package org.grupob.empapp.service;

import org.grupob.comun.entity.Colaboracion;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.SolicitudColaboracion;
import org.grupob.comun.entity.auxiliar.Periodo;
import org.grupob.comun.entity.maestras.Estado;
import org.grupob.comun.exception.EmpleadoNoEncontradoException;
import org.grupob.comun.repository.ColaboracionRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.SolicitudColaboracionRepository;
import org.grupob.comun.repository.maestras.EstadoRepository;
import org.grupob.empapp.converter.ColaboracionesConverter; // Tu converter
import org.grupob.empapp.converter.SolicitudColaboracionConverter; // Nuevo converter
// import org.grupob.empapp.converter.PeriodoConverter; // No se usa directamente aquí, sino dentro de ColaboracionesConverter
import org.grupob.empapp.dto.ColaboracionEstablecidaDTO;
// PeriodoDTO ya no se usa directamente aquí para el mapeo
import org.grupob.empapp.dto.HistorialColaboracionItemDTO;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate; //Cambiado a LocalDateTime si es necesario
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ColaboracionServiceImp implements ColaboracionService {

    private final EmpleadoRepository empleadoRepository;
    private final SolicitudColaboracionRepository solicitudColaboracionRepository;
    private final ColaboracionRepository colaboracionRepository;
    private final EstadoRepository estadoRepository;
    private final ColaboracionesConverter colaboracionesConverter; // Inyectar
    private final SolicitudColaboracionConverter solicitudColaboracionConverter; // Inyectar
    private static final long COOLDOWN_MINUTOS_TRAS_FINALIZACION = 2;

    @Autowired
    public ColaboracionServiceImp(EmpleadoRepository empleadoRepository,
                                  SolicitudColaboracionRepository solicitudColaboracionRepository,
                                  ColaboracionRepository colaboracionRepository,
                                  EstadoRepository estadoRepository,
                                  ColaboracionesConverter colaboracionesConverter, // Añadir
                                  SolicitudColaboracionConverter solicitudColaboracionConverter) { // Añadir
        this.empleadoRepository = empleadoRepository;
        this.solicitudColaboracionRepository = solicitudColaboracionRepository;
        this.colaboracionRepository = colaboracionRepository;
        this.estadoRepository = estadoRepository;
        this.colaboracionesConverter = colaboracionesConverter; // Asignar
        this.solicitudColaboracionConverter = solicitudColaboracionConverter; // Asignar
    }

    @Override
    public List<ColaboracionEstablecidaDTO> getColaboracionesEstablecidas(UUID idEmpleadoActual) {
        if (!empleadoRepository.existsById(idEmpleadoActual)) {
            throw new EmpleadoNoEncontradoException("Empleado actual no encontrado con ID: " + idEmpleadoActual);
        }
        List<Colaboracion> colaboracionesDelEmpleado = colaboracionRepository.findAllByEmpleadoId(idEmpleadoActual);
        // Este es el comportamiento original que solo devuelve ColaboracionEstablecidaDTO
        return colaboracionesConverter.toDtoList(colaboracionesDelEmpleado, idEmpleadoActual);
    }

    @Override
    @Transactional
    public void enviarSolicitudColaboracion(UUID idSolicitante, UUID idReceptor) throws Exception {
//        logger.debug("Intentando enviar solicitud de colaboración de {} a {}", idSolicitante, idReceptor);

        Empleado solicitante = empleadoRepository.findById(idSolicitante)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Solicitante no encontrado con ID: " + idSolicitante));
        Empleado receptor = empleadoRepository.findById(idReceptor)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Receptor no encontrado con ID: " + idReceptor));

        if (idSolicitante.equals(idReceptor)) {
//            logger.warn("Intento de auto-solicitud por empleado ID: {}", idSolicitante);
            throw new IllegalArgumentException("Un empleado no puede enviarse una solicitud de colaboración a sí mismo.");
        }

        // 1. Verificar si ya existe una colaboración ACTIVA
        Optional<Colaboracion> colaboracionActivaOpt = colaboracionRepository.findColaboracionEntreEmpleados(solicitante, receptor)
                .filter(c -> c.getPeriodoActivo().isPresent());
        if (colaboracionActivaOpt.isPresent()) {
//            logger.info("Intento de solicitud entre {} y {} denegado: ya existe una colaboración activa (ID: {}).", idSolicitante, idReceptor, colaboracionActivaOpt.get().getId());
            throw new Exception("Ya existe una colaboración activa con este empleado.");
        }

        // 2. Verificar si ya existe una solicitud PENDIENTE
        Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Error de configuración: Estado PENDIENTE no definido."));
        Optional<SolicitudColaboracion> solicitudPendienteEnviada = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado(solicitante, receptor, estadoPendiente);
        if (solicitudPendienteEnviada.isPresent()) {
            throw new Exception("Ya has enviado una solicitud pendiente a este empleado. Espera su respuesta.");
        }
        Optional<SolicitudColaboracion> solicitudPendienteRecibida = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado(receptor, solicitante, estadoPendiente);
        if (solicitudPendienteRecibida.isPresent()) {
            throw new Exception("Ya tienes una solicitud pendiente de este empleado. Por favor, acéptala o recházala.");
        }

        // 3. Verificar COOLDOWN usando fechaHoraUltimaFinalizacion de la Colaboracion
        Optional<Colaboracion> colaboracionPreviaOpt = colaboracionRepository.findColaboracionEntreEmpleados(solicitante, receptor);
        if (colaboracionPreviaOpt.isPresent()) {
            Colaboracion colaboracionPrevia = colaboracionPreviaOpt.get();
            if (colaboracionPrevia.getFechaHoraUltimaFinalizacion() != null) { // <-- USAR ESTE CAMPO
                LocalDateTime fechaUltimaFinalizacionPrecisa = colaboracionPrevia.getFechaHoraUltimaFinalizacion();
                LocalDateTime tiempoPermitidoParaNuevaSolicitud = fechaUltimaFinalizacionPrecisa.plusMinutes(COOLDOWN_MINUTOS_TRAS_FINALIZACION);

                if (LocalDateTime.now().isBefore(tiempoPermitidoParaNuevaSolicitud)) {
//                    logger.info("Intento de solicitud de {} a {} denegado: Cooldown activo hasta {}.",
//                            idSolicitante, idReceptor, tiempoPermitidoParaNuevaSolicitud.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    throw new Exception("Debes esperar hasta " +
                            tiempoPermitidoParaNuevaSolicitud.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                            " para enviar una nueva solicitud a este empleado después de la última finalización de colaboración.");
                }
            }
        }

        // 4. Lógica de bloqueo por rechazo previo de solicitud
        Optional<SolicitudColaboracion> solicitudesRechazadasPorReceptor = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado_Nombre(solicitante, receptor, "RECHAZADA");
        Optional<SolicitudColaboracion> bloqueoActivoPorRechazo = solicitudesRechazadasPorReceptor.stream()
                .filter(s -> s.getFecha_desbloqueo() != null && LocalDateTime.now().isBefore(s.getFecha_desbloqueo()))
                .max(Comparator.comparing(SolicitudColaboracion::getFecha_desbloqueo));

        if (bloqueoActivoPorRechazo.isPresent()) {
            SolicitudColaboracion solicitudBloqueante = bloqueoActivoPorRechazo.get();
            throw new Exception("No puedes enviar una solicitud a este empleado porque una solicitud reciente fue rechazada por ellos. " +
                    "Podrás intentarlo de nuevo después del " +
                    solicitudBloqueante.getFecha_desbloqueo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".");
        }

        // Crear la nueva solicitud
        SolicitudColaboracion nuevaSolicitud = new SolicitudColaboracion();
        nuevaSolicitud.setSolicitante(solicitante);
        nuevaSolicitud.setReceptor(receptor);
        nuevaSolicitud.setFechaSolicitud(LocalDateTime.now());
        nuevaSolicitud.setEstado(estadoPendiente);
        solicitudColaboracionRepository.save(nuevaSolicitud);
//        logger.info("Nueva solicitud de colaboración (ID: {}) creada y enviada de {} a {}.", nuevaSolicitud.getId(), idSolicitante, idReceptor);
    }

    @Override
    public List<SolicitudColaboracionDTO> getSolicitudesRecibidas(UUID idEmpleado) {
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + idEmpleado));

        // No es necesario buscar el estado aquí si el repo lo hace por nombre
        List<SolicitudColaboracion> solicitudes = solicitudColaboracionRepository.findByReceptorAndEstadoNombre(empleado, "PENDIENTE");
        if (solicitudes.isEmpty()) {
            return Collections.emptyList();
        }
        return solicitudes.stream()
                .map(solicitudColaboracionConverter::toDtoParaRecibidas) // Usar el método específico
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudColaboracionDTO> getSolicitudesEnviadas(UUID idEmpleado) {
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + idEmpleado));
        List<SolicitudColaboracion> solicitudes = solicitudColaboracionRepository.findBySolicitante(empleado);
        if (solicitudes.isEmpty()) {
            return Collections.emptyList();
        }
        return solicitudes.stream()
                .map(solicitudColaboracionConverter::toDtoParaEnviadas) // Usar el método específico
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void aceptarSolicitud(UUID idSolicitud, UUID idReceptorActual) throws Exception {
        // ... (validaciones iniciales como antes) ...
        SolicitudColaboracion solicitud = solicitudColaboracionRepository.findById(idSolicitud)
                .orElseThrow(() -> new Exception("Solicitud no encontrada con ID: " + idSolicitud));
        // ... (más validaciones) ...

        Estado estadoAceptada = estadoRepository.findByNombre("ACEPTADA")
                .orElseThrow(() -> new RuntimeException("Estado ACEPTADA no encontrado."));
        solicitud.setEstado(estadoAceptada);
        solicitud.setFechaAceptacion(LocalDateTime.now());
        solicitudColaboracionRepository.save(solicitud);

        Empleado empleadoA = solicitud.getSolicitante();
        Empleado empleadoB = solicitud.getReceptor();
        Optional<Colaboracion> optColaboracion = colaboracionRepository.findColaboracionEntreEmpleados(empleadoA, empleadoB);
        Colaboracion colaboracion;

        if (optColaboracion.isPresent()) {
            colaboracion = optColaboracion.get();
//            logger.info("Colaboración existente (ID: {}). Añadiendo nuevo periodo.", colaboracion.getId());
        } else {
            colaboracion = new Colaboracion();
            colaboracion.setEmisor(empleadoA);
            colaboracion.setReceptor(empleadoB);
            colaboracion.setFechaCreacion(LocalDateTime.now());
            colaboracion.setPeriodos(new java.util.ArrayList<>()); // Inicializar lista
//            logger.info("Creando nueva colaboración.");
        }
        // Limpiar la fechaHoraUltimaFinalizacion si la colaboración se está reactivando
        colaboracion.setFechaHoraUltimaFinalizacion(null);


        Periodo nuevoPeriodo = new Periodo();
        // Si Periodo.fechaInicio es LocalDate
        nuevoPeriodo.setFechaInicio(LocalDate.now()); // Mantener LocalDateTime para inicio de periodo por precisión
        // O si debe ser LocalDate: nuevoPeriodo.setFechaInicio(LocalDate.now());
        nuevoPeriodo.setFechaFin(null); // Activo
        if (colaboracion.getPeriodos() == null) {
            colaboracion.setPeriodos(new java.util.ArrayList<>());
        }
        colaboracion.getPeriodos().add(nuevoPeriodo);
        colaboracionRepository.save(colaboracion);
//        logger.info("Nuevo periodo activo añadido a la colaboración ID {}.", colaboracion.getId());
    }

    @Override
    @Transactional
    public void rechazarSolicitud(UUID idSolicitud, UUID idReceptorActual) throws Exception {
        SolicitudColaboracion solicitud = solicitudColaboracionRepository.findById(idSolicitud)
                .orElseThrow(() -> new Exception("Solicitud no encontrada con ID: " + idSolicitud));

        if (!solicitud.getReceptor().getId().equals(idReceptorActual)) {
            throw new SecurityException("No tienes permiso para rechazar esta solicitud.");
        }
        if (!"PENDIENTE".equalsIgnoreCase(solicitud.getEstado().getNombre())) {
            throw new IllegalStateException("Solo se pueden rechazar solicitudes en estado PENDIENTE.");
        }

        Estado estadoRechazada = estadoRepository.findByNombre("RECHAZADA")
                .orElseThrow(() -> new RuntimeException("Estado RECHAZADA no encontrado en la base de datos."));

        solicitud.setEstado(estadoRechazada);
        solicitud.setFechaRechazo(LocalDateTime.now());
        solicitud.setFecha_desbloqueo(LocalDateTime.now().plusMinutes(2)); // Periodo de bloqueo
        solicitudColaboracionRepository.save(solicitud);
    }

    @Override
    public List<Empleado> getOtrosEmpleados(UUID idEmpleadoActual) {
        return empleadoRepository.findAll().stream()
                .filter(e -> !e.getId().equals(idEmpleadoActual) && e.isActivo())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<HistorialColaboracionItemDTO> getHistorialCompletoColaboraciones(UUID idEmpleadoActual) {
//        logger.debug("Obteniendo historial completo de colaboraciones para empleado ID: {}", idEmpleadoActual);

        Empleado empleadoActualValidado = empleadoRepository.findById(idEmpleadoActual)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado actual no encontrado con ID: " + idEmpleadoActual));

        // 1. Obtener Colaboraciones Establecidas como List<ColaboracionEstablecidaDTO>
        List<Colaboracion> colaboracionesEntidades = colaboracionRepository.findAllByEmpleadoId(idEmpleadoActual);
        List<ColaboracionEstablecidaDTO> colaboracionesEstablecidasDtos =
                colaboracionesConverter.toDtoList(colaboracionesEntidades, idEmpleadoActual);

        // Convertir List<ColaboracionEstablecidaDTO> a List<HistorialColaboracionItemDTO>
        // USANDO EL CONSTRUCTOR CORRECTO:
        List<HistorialColaboracionItemDTO> historialEstablecidas = colaboracionesEstablecidasDtos.stream()
                .map(HistorialColaboracionItemDTO::new) // Llama al constructor que toma ColaboracionEstablecidaDTO
                .collect(Collectors.toList());
//        logger.debug("Número de colaboraciones establecidas procesadas: {}", historialEstablecidas.size());


        // 2. Obtener Solicitudes Enviadas
        List<SolicitudColaboracion> solicitudesEnviadasEntidades =
                solicitudColaboracionRepository.findBySolicitante(empleadoActualValidado);

        List<HistorialColaboracionItemDTO> historialEnviadas = solicitudesEnviadasEntidades.stream()
                .map(solicitudColaboracionConverter::toDtoParaEnviadas)
                .map(dto -> new HistorialColaboracionItemDTO(dto, true))
                .collect(Collectors.toList());
//        logger.debug("Número de solicitudes enviadas procesadas: {}", historialEnviadas.size());


        // 3. Obtener Solicitudes Recibidas
        List<SolicitudColaboracion> solicitudesRecibidasEntidades =
                solicitudColaboracionRepository.findByReceptor(empleadoActualValidado);

        List<HistorialColaboracionItemDTO> historialRecibidas = solicitudesRecibidasEntidades.stream()
                .map(solicitudColaboracionConverter::toDtoParaRecibidas)
                .map(dto -> new HistorialColaboracionItemDTO(dto, false))
                .collect(Collectors.toList());
//        logger.debug("Número de solicitudes recibidas procesadas: {}", historialRecibidas.size());


        // 4. Combinar todas las listas
        List<HistorialColaboracionItemDTO> historialCompleto = Stream.of(
                        historialEstablecidas,
                        historialEnviadas,
                        historialRecibidas)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 5. Ordenar el historial completo
        historialCompleto.sort(Comparator.comparing(
                HistorialColaboracionItemDTO::getFechaEventoPrincipal,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));
//        logger.info("Historial completo combinado y ordenado para empleado ID: {}. Total de items: {}", idEmpleadoActual, historialCompleto.size());

        return historialCompleto;
    }

    @Override
    @Transactional
    public void finalizarPeriodoColaboracion(UUID idColaboracion, UUID idEmpleadoFinalizador) throws Exception {
//        logger.info("Intentando finalizar periodo para colaboración ID: {} por empleado ID: {}", idColaboracion, idEmpleadoFinalizador);

        // Validar que el empleado que finaliza existe (aunque no se usa directamente después, es una buena práctica)
        if (!empleadoRepository.existsById(idEmpleadoFinalizador)) {
//            logger.warn("Empleado finalizador con ID: {} no encontrado.", idEmpleadoFinalizador);
            throw new EmpleadoNoEncontradoException("Empleado que intenta finalizar no encontrado con ID: " + idEmpleadoFinalizador);
        }

        Colaboracion colaboracion = colaboracionRepository.findById(idColaboracion)
                .orElseThrow(() -> {
//                    logger.warn("Colaboración no encontrada con ID: {} al intentar finalizar periodo.", idColaboracion);
                    return new NoSuchElementException("Colaboración no encontrada con ID: " + idColaboracion);
                });

        // Verificar que el empleado actual es parte de la colaboración
        boolean esEmisor = colaboracion.getEmisor() != null && colaboracion.getEmisor().getId().equals(idEmpleadoFinalizador);
        boolean esReceptor = colaboracion.getReceptor() != null && colaboracion.getReceptor().getId().equals(idEmpleadoFinalizador);

        if (!esEmisor && !esReceptor) {
//            logger.warn("Intento no autorizado de finalizar colaboración ID {} por empleado ID {} que no es participante.", idColaboracion, idEmpleadoFinalizador);
            throw new SecurityException("El empleado actual no es parte de esta colaboración y no puede finalizarla.");
        }

        Optional<Periodo> periodoActivoOpt = colaboracion.getPeriodoActivo();
        if (periodoActivoOpt.isEmpty()) {
//            logger.warn("No hay periodo activo para finalizar en colaboración ID {}. La colaboración ya podría estar inactiva.", idColaboracion);
            throw new IllegalStateException("No hay un periodo activo para finalizar en esta colaboración. Es posible que ya haya sido finalizada.");
        }

        Periodo periodoActivo = periodoActivoOpt.get();

        // Si el periodo ya tiene fecha de fin, no hacer nada o lanzar error.
        if (periodoActivo.getFechaFin() != null) {
//            logger.warn("El periodo activo de la colaboración ID {} ya tiene una fecha de fin: {}. No se puede finalizar de nuevo.", idColaboracion, periodoActivo.getFechaFin());
            throw new IllegalStateException("El periodo actual de esta colaboración ya ha sido finalizado previamente.");
        }

        periodoActivo.setFechaFin(LocalDate.now()); // Establece la fecha de fin del periodo

        // Actualizar el timestamp preciso en la entidad Colaboracion para el cooldown
        LocalDateTime ahora = LocalDateTime.now();
        colaboracion.setFechaHoraUltimaFinalizacion(ahora);

        colaboracionRepository.save(colaboracion); // Guardar la colaboración con el periodo actualizado y la fecha de finalización

//        logger.info("Periodo de colaboración (ID Colaboracion: {}) finalizado correctamente. FechaFin del periodo: {}. Colaboracion.fechaHoraUltimaFinalizacion establecida a: {}",
//                idColaboracion, periodoActivo.getFechaFin(), colaboracion.getFechaHoraUltimaFinalizacion());

        // Log de verificación (opcional, útil para depuración)
        Colaboracion colaboracionGuardada = colaboracionRepository.findById(idColaboracion).orElse(null);
        if (colaboracionGuardada != null && colaboracionGuardada.getFechaHoraUltimaFinalizacion() != null) {
            if (!colaboracionGuardada.getFechaHoraUltimaFinalizacion().equals(ahora)) {
//                logger.error("DISCREPANCIA POST-GUARDADO: Colaboracion ID {} - fechaHoraUltimaFinalizacion guardada ({}) no es igual a la que se intentó guardar ({})",
//                        idColaboracion, colaboracionGuardada.getFechaHoraUltimaFinalizacion(), ahora);
            } else {
//                logger.debug("VERIFICACIÓN POST-GUARDADO: Colaboracion ID {} - fechaHoraUltimaFinalizacion guardada correctamente: {}",
//                        idColaboracion, colaboracionGuardada.getFechaHoraUltimaFinalizacion());
            }
        } else {
//            logger.error("ERROR DE VERIFICACIÓN POST-GUARDADO: Colaboracion ID {} - NO tiene fechaHoraUltimaFinalizacion después de guardar, o la colaboración no se encontró.", idColaboracion);
        }
    }
}