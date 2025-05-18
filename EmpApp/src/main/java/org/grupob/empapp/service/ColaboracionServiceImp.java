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
        Empleado solicitante = empleadoRepository.findById(idSolicitante)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Solicitante no encontrado"));
        Empleado receptor = empleadoRepository.findById(idReceptor)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Receptor no encontrado"));

        if (idSolicitante.equals(idReceptor)) {
            throw new IllegalArgumentException("Un empleado no puede enviarse una solicitud de colaboración a sí mismo.");
        }

        Optional<Colaboracion> colaboracionExistente = colaboracionRepository.findColaboracionEntreEmpleados(solicitante, receptor);
        if (colaboracionExistente.isPresent() && colaboracionExistente.get().getPeriodoActivo().isPresent()) {
            throw new Exception("Ya existe una colaboración activa con este empleado.");
        }

        Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado en la base de datos."));

        Optional<SolicitudColaboracion> solicitudPendienteExistente = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado_Nombre(solicitante, receptor, "PENDIENTE");
        if (solicitudPendienteExistente.isPresent()) {
            throw new Exception("Ya existe una solicitud pendiente para este empleado.");
        }

        // Lógica de bloqueo por rechazo previo
        Optional<SolicitudColaboracion> solicitudRechazadaReciente = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado_Nombre(solicitante, receptor, "RECHAZADA")
                .filter(s -> s.getFecha_desbloqueo() != null && s.getFecha_desbloqueo().isAfter(LocalDateTime.now())); // Asumimos que solo nos importa la más reciente si hay varias

        if (solicitudRechazadaReciente.isPresent()) {
            throw new Exception("No puedes enviar una solicitud a este empleado porque una solicitud reciente fue rechazada. Intenta más tarde (después de " +
                    solicitudRechazadaReciente.get().getFecha_desbloqueo().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ").");
        }


        SolicitudColaboracion nuevaSolicitud = new SolicitudColaboracion();
        nuevaSolicitud.setSolicitante(solicitante);
        nuevaSolicitud.setReceptor(receptor);
        nuevaSolicitud.setFechaSolicitud(LocalDateTime.now());
        nuevaSolicitud.setEstado(estadoPendiente);
        solicitudColaboracionRepository.save(nuevaSolicitud);
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
        SolicitudColaboracion solicitud = solicitudColaboracionRepository.findById(idSolicitud)
                .orElseThrow(() -> new Exception("Solicitud no encontrada con ID: " + idSolicitud));

        if (!solicitud.getReceptor().getId().equals(idReceptorActual)) {
            throw new SecurityException("No tienes permiso para aceptar esta solicitud."); // Más apropiado
        }
        if (!"PENDIENTE".equalsIgnoreCase(solicitud.getEstado().getNombre())) {
            throw new IllegalStateException("Solo se pueden aceptar solicitudes en estado PENDIENTE.");
        }

        Estado estadoAceptada = estadoRepository.findByNombre("ACEPTADA")
                .orElseThrow(() -> new RuntimeException("Estado ACEPTADA no encontrado en la base de datos."));

        solicitud.setEstado(estadoAceptada);
        solicitud.setFechaAceptacion(LocalDateTime.now());
        // solicitud.setFecha_desbloqueo(null); // Limpiar fecha de desbloqueo si aplica
        solicitudColaboracionRepository.save(solicitud);

        Empleado empleadoA = solicitud.getSolicitante();
        Empleado empleadoB = solicitud.getReceptor();

        Optional<Colaboracion> optColaboracion = colaboracionRepository.findColaboracionEntreEmpleados(empleadoA, empleadoB);
        Colaboracion colaboracion;
        if (optColaboracion.isPresent()) {
            colaboracion = optColaboracion.get();
            // Si ya existe, asegurarse de que no haya un periodo activo antes de añadir uno nuevo (o manejar lógica de reanudar)
            if (colaboracion.getPeriodoActivo().isPresent()) {
                // Esto podría ser un error o una lógica de negocio no contemplada.
                // Por ahora, simplemente no creamos un nuevo periodo si ya hay uno activo.
                // O podríamos cerrar el antiguo y abrir uno nuevo. Depende del requisito.
                // throw new IllegalStateException("La colaboración ya tiene un periodo activo.");
                System.out.println("Advertencia: La colaboración ya tiene un periodo activo. No se crea nuevo periodo.");
                return; // o manejar de otra forma
            }
        } else {
            colaboracion = new Colaboracion();
            colaboracion.setEmisor(empleadoA);
            colaboracion.setReceptor(empleadoB);
            colaboracion.setFechaCreacion(LocalDateTime.now());
            colaboracion.setPeriodos(new ArrayList<>());
        }

        Periodo nuevoPeriodo = new Periodo();
        nuevoPeriodo.setFechaInicio(LocalDate.now()); // Cambiado a LocalDateTime para consistencia
        // nuevoPeriodo.setIniciadoPor(empleadoA); // Si añades este campo a la entidad Periodo
        if (colaboracion.getPeriodos() == null) { // Defensa contra NPE
            colaboracion.setPeriodos(new ArrayList<>());
        }
        colaboracion.getPeriodos().add(nuevoPeriodo);
        colaboracionRepository.save(colaboracion);
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
        solicitud.setFecha_desbloqueo(LocalDateTime.now().plusDays(7)); // Periodo de bloqueo
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
        // Validar empleado
        if (!empleadoRepository.existsById(idEmpleadoActual)) {
            throw new EmpleadoNoEncontradoException("Empleado actual no encontrado con ID: " + idEmpleadoActual);
        }

        // 1. Obtener Colaboraciones Establecidas (usando el converter existente)
        List<Colaboracion> colaboracionesEntidades = colaboracionRepository.findAllByEmpleadoId(idEmpleadoActual);
        List<HistorialColaboracionItemDTO> historialEstablecidas = colaboracionesConverter
                .toDtoList(colaboracionesEntidades, idEmpleadoActual) // Este método ya devuelve List<ColaboracionEstablecidaDTO>
                .stream()
                .map(HistorialColaboracionItemDTO::new) // Convertir ColaboracionEstablecidaDTO a HistorialColaboracionItemDTO
                .collect(Collectors.toList());

        // 2. Obtener Solicitudes Enviadas (PENDIENTES y otras si quieres)
        Empleado empleado = new Empleado(); // Crear instancia temporal para el repositorio
        empleado.setId(idEmpleadoActual);   // Establecer solo el ID

        List<SolicitudColaboracion> solicitudesEnviadasEntidades = solicitudColaboracionRepository.findBySolicitante(empleado);
        List<HistorialColaboracionItemDTO> historialEnviadas = solicitudesEnviadasEntidades.stream()
                // Filtra aquí si solo quieres PENDIENTES o también RECHAZADAS, etc.
                // .filter(s -> "PENDIENTE".equalsIgnoreCase(s.getEstado().getNombre()))
                .map(sol -> solicitudColaboracionConverter.toDtoParaEnviadas(sol)) // Usa tu converter
                .map(dto -> new HistorialColaboracionItemDTO(dto, true)) // true indica que es enviada
                .collect(Collectors.toList());

        // 3. Obtener Solicitudes Recibidas (PENDIENTES y otras si quieres)
        List<SolicitudColaboracion> solicitudesRecibidasEntidades = solicitudColaboracionRepository.findByReceptor(empleado);
        List<HistorialColaboracionItemDTO> historialRecibidas = solicitudesRecibidasEntidades.stream()
                // Filtra aquí si solo quieres PENDIENTES
                // .filter(s -> "PENDIENTE".equalsIgnoreCase(s.getEstado().getNombre()))
                .map(sol -> solicitudColaboracionConverter.toDtoParaRecibidas(sol)) // Usa tu converter
                .map(dto -> new HistorialColaboracionItemDTO(dto, false)) // false indica que es recibida
                .collect(Collectors.toList());

        // 4. Combinar todas las listas
        List<HistorialColaboracionItemDTO> historialCompleto = Stream.of(
                        historialEstablecidas,
                        historialEnviadas,
                        historialRecibidas)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Eliminar duplicados si una solicitud aceptada también aparece como colaboración
        // (esto depende de si las solicitudes se eliminan o cambian de estado al aceptarse)
        // Si una solicitud PENDIENTE se convierte en ACEPTADA y luego en una COLABORACION,
        // podrías tener la misma interacción representada dos veces.
        // Una forma simple de manejarlo es filtrar las solicitudes ACEPTADAS si ya hay una colaboración
        // O, más robusto, basar la unicidad en la combinación de tipo e idReferencia,
        // y luego priorizar la "COLABORACION_ESTABLECIDA".
        // Por ahora, vamos a ordenarlas.

        // 5. Ordenar el historial completo (ej. por fecha del evento principal descendente)
        historialCompleto.sort(Comparator.comparing(HistorialColaboracionItemDTO::getFechaEventoPrincipal, Comparator.nullsLast(Comparator.reverseOrder())));

        return historialCompleto;
    }
}