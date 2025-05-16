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
import org.grupob.empapp.dto.SolicitudColaboracionDTO; // Asegúrate que el path es correcto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ColaboracionServiceImp implements ColaboracionService {

    private final EmpleadoRepository empleadoRepository;
    private final SolicitudColaboracionRepository solicitudColaboracionRepository;
    private final ColaboracionRepository colaboracionRepository;
    private final EstadoRepository estadoRepository;
    // private final ModelMapper modelMapper; // Si usas ModelMapper para convertir DTOs

    @Autowired
    public ColaboracionServiceImp(EmpleadoRepository empleadoRepository,
                               SolicitudColaboracionRepository solicitudColaboracionRepository,
                               ColaboracionRepository colaboracionRepository,
                               EstadoRepository estadoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.solicitudColaboracionRepository = solicitudColaboracionRepository;
        this.colaboracionRepository = colaboracionRepository;
        this.estadoRepository = estadoRepository;
    }

    @Transactional
    public void enviarSolicitudColaboracion(UUID idSolicitante, UUID idReceptor) throws Exception {
        Empleado solicitante = empleadoRepository.findById(idSolicitante)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Solicitante no encontrado"));
        Empleado receptor = empleadoRepository.findById(idReceptor)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Receptor no encontrado"));

        // Verificar si ya existe una colaboración activa o una solicitud pendiente (según PDF 2.1.1.b)
        Optional<Colaboracion> colaboracionExistente = colaboracionRepository.findColaboracionEntreEmpleados(solicitante, receptor);
        if (colaboracionExistente.isPresent() && colaboracionExistente.get().getPeriodoActivo().isPresent()) {
            throw new Exception("Ya existe una colaboración activa con este empleado.");
        }

        Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new Exception("Estado PENDIENTE no encontrado"));

        Optional<SolicitudColaboracion> solicitudExistente = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado_Nombre(solicitante, receptor, "PENDIENTE");
        if (solicitudExistente.isPresent()) {
            throw new Exception("Ya existe una solicitud pendiente para este empleado.");
        }
        // Verificar condiciones de bloqueo por rechazo o cancelación reciente (PDF 2.1.1.c, 2.1.1.d)
        // Esta lógica requiere guardar fecha_desbloqueo en SolicitudColaboracion
        // y potencialmente una entidad o campos para el "bloqueo de colaboración" global.
        // Ejemplo simplificado:
        List<SolicitudColaboracion> solicitudesRechazadasPrevias = solicitudColaboracionRepository
                .findBySolicitanteAndReceptorAndEstado_Nombre(solicitante, receptor, "RECHAZADA")
                .stream()
                .filter(s -> s.getFecha_desbloqueo() != null && s.getFecha_desbloqueo().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (!solicitudesRechazadasPrevias.isEmpty()) {
            throw new Exception("No puedes enviar una solicitud a este empleado porque una solicitud reciente fue rechazada. Intenta más tarde.");
        }
        // Aquí también iría la lógica para la cancelación reciente.

        SolicitudColaboracion nuevaSolicitud = new SolicitudColaboracion();
        nuevaSolicitud.setSolicitante(solicitante);
        nuevaSolicitud.setReceptor(receptor);
        nuevaSolicitud.setFechaSolicitud(LocalDateTime.now());
        nuevaSolicitud.setEstado(estadoPendiente);
        solicitudColaboracionRepository.save(nuevaSolicitud);
    }

    public List<SolicitudColaboracionDTO> getSolicitudesRecibidas(UUID idEmpleado) {
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado"));
        Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE").orElse(null);
        if (estadoPendiente == null) return List.of(); // O manejar error

        return solicitudColaboracionRepository.findByReceptorAndEstadoNombre(empleado, "PENDIENTE")
                .stream()
                .map(s -> new SolicitudColaboracionDTO(
                        s.getId(),
                        s.getSolicitante().getNombre() + " " + s.getSolicitante().getApellido(),
                        s.getFechaSolicitud(),
                        s.getEstado().getNombre(),
                        s.getSolicitante().getId()
                ))
                .collect(Collectors.toList());
    }

    public List<SolicitudColaboracionDTO> getSolicitudesEnviadas(UUID idEmpleado) {
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado"));
        return solicitudColaboracionRepository.findBySolicitante(empleado) // Podrías filtrar por estado si es necesario
                .stream()
                .map(s -> new SolicitudColaboracionDTO(
                        s.getId(),
                        s.getReceptor().getNombre() + " " + s.getReceptor().getApellido(),
                        s.getFechaSolicitud(),
                        s.getEstado().getNombre()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void aceptarSolicitud(UUID idSolicitud, UUID idReceptorActual) throws Exception {
        SolicitudColaboracion solicitud = solicitudColaboracionRepository.findById(idSolicitud)
                .orElseThrow(() -> new Exception("Solicitud no encontrada"));

        if (!solicitud.getReceptor().getId().equals(idReceptorActual)) {
            throw new Exception("No tienes permiso para aceptar esta solicitud.");
        }

        Estado estadoAceptada = estadoRepository.findByNombre("ACEPTADA")
                .orElseThrow(() -> new Exception("Estado ACEPTADA no encontrado"));

        solicitud.setEstado(estadoAceptada);
        solicitud.setFechaAceptacion(LocalDateTime.now());
        solicitudColaboracionRepository.save(solicitud);

        // Crear o actualizar colaboración (PDF 1.1.2)
        Empleado empleadoA = solicitud.getSolicitante();
        Empleado empleadoB = solicitud.getReceptor();

        Optional<Colaboracion> optColaboracion = colaboracionRepository.findColaboracionEntreEmpleados(empleadoA, empleadoB);
        Colaboracion colaboracion;
        if (optColaboracion.isPresent()) {
            colaboracion = optColaboracion.get();
        } else {
            colaboracion = new Colaboracion();
            colaboracion.setEmisor(empleadoA); // O solicitante
            colaboracion.setReceptor(empleadoB); // O receptor
            colaboracion.setFechaCreacion(LocalDateTime.now());
            colaboracion.setPeriodos(new java.util.ArrayList<>());
        }

        // Añadir nuevo periodo de colaboración
        Periodo nuevoPeriodo = new Periodo();
        nuevoPeriodo.setFechaInicio(LocalDate.now());
        // Según el PDF, se debe registrar quién arrancó la colaboración.
        // Asumimos que es el solicitante original.
        // Podrías añadir un campo `iniciadoPor` a la entidad `Periodo`.
        colaboracion.getPeriodos().add(nuevoPeriodo);
        colaboracionRepository.save(colaboracion);
    }

    @Transactional
    public void rechazarSolicitud(UUID idSolicitud, UUID idReceptorActual) throws Exception {
        SolicitudColaboracion solicitud = solicitudColaboracionRepository.findById(idSolicitud)
                .orElseThrow(() -> new Exception("Solicitud no encontrada"));

        if (!solicitud.getReceptor().getId().equals(idReceptorActual)) {
            throw new Exception("No tienes permiso para rechazar esta solicitud.");
        }

        Estado estadoRechazada = estadoRepository.findByNombre("RECHAZADA")
                .orElseThrow(() -> new Exception("Estado RECHAZADA no encontrado"));

        solicitud.setEstado(estadoRechazada);
        solicitud.setFechaRechazo(LocalDateTime.now());

        // Lógica de bloqueo (PDF 1.1.3)
        // "A no pueda enviar una nueva solicitud de colaboración a B en un período de 'bloqueo de colaboración'"
        // Esto implica que el solicitante (A) queda bloqueado para solicitar a B.
        // Podrías configurar un tiempo de bloqueo, por ejemplo, 7 días.
        solicitud.setFecha_desbloqueo(LocalDateTime.now().plusDays(7)); // Ejemplo de bloqueo

        solicitudColaboracionRepository.save(solicitud);
    }

    public List<Empleado> getOtrosEmpleados(UUID idEmpleadoActual) {
        return empleadoRepository.findAll().stream()
                .filter(e -> !e.getId().equals(idEmpleadoActual) && e.isActivo())
                .collect(Collectors.toList());
    }
}