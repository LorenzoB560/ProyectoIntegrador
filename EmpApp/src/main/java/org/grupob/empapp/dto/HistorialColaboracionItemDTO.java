package org.grupob.empapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class HistorialColaboracionItemDTO {
    private String tipo; // "COLABORACION_ESTABLECIDA", "SOLICITUD_ENVIADA", "SOLICITUD_RECIBIDA"
    private UUID idReferencia; // ID de la Colaboracion o SolicitudColaboracion
    private String nombreOtroEmpleado;
    private UUID idOtroEmpleado;
    private LocalDateTime fechaEventoPrincipal; // Fecha de creación de colaboración o fecha de solicitud
    private String estadoActual; // "ACTIVA", "FINALIZADA", "PENDIENTE", "RECHAZADA" (para solicitudes)

    // Campos específicos para colaboraciones establecidas
    private List<PeriodoDTO> periodos; // Usaremos tu PeriodoDTO existente
    private boolean actualmenteActiva;
    private boolean puedeFinalizar;

    // Campos específicos para solicitudes (si se necesitan más detalles además del estado)
    // Por ejemplo, si quieres mostrar la fecha de aceptación/rechazo en el futuro
    // private LocalDateTime fechaRespuestaSolicitud;

    // Constructores específicos pueden ser útiles
    // Constructor para Colaboración Establecida
    public HistorialColaboracionItemDTO(ColaboracionEstablecidaDTO colabEst) {
        this.tipo = "COLABORACION_ESTABLECIDA";
        this.idReferencia = colabEst.getIdColaboracion();
        this.nombreOtroEmpleado = colabEst.getOtroEmpleadoNombreCompleto();
        this.idOtroEmpleado = colabEst.getOtroEmpleadoId();
        this.fechaEventoPrincipal = colabEst.getFechaCreacionColaboracion();
        this.estadoActual = !colabEst.isActualmenteActiva() ? "FINALIZADA" : "ACTIVA";
        this.periodos = colabEst.getPeriodos();
        this.actualmenteActiva = colabEst.isActualmenteActiva();
        this.puedeFinalizar = colabEst.isActualmenteActiva();

    }

    // Constructor para Solicitud Enviada
    public HistorialColaboracionItemDTO(SolicitudColaboracionDTO solEnv, boolean esEnviada) {
        this.tipo = esEnviada ? "SOLICITUD_ENVIADA" : "SOLICITUD_RECIBIDA";
        this.idReferencia = solEnv.getId();
        this.nombreOtroEmpleado = esEnviada ? solEnv.getReceptorNombre() : solEnv.getSolicitanteNombre();
        this.idOtroEmpleado = esEnviada ? solEnv.getReceptorId() : solEnv.getSolicitanteId(); // Asegúrate que SolicitudColaboracionDTO tiene estos IDs
        this.fechaEventoPrincipal = solEnv.getFechaSolicitud();
        this.estadoActual = solEnv.getEstado(); // ej. "PENDIENTE", "ACEPTADA", "RECHAZADA"
        this.periodos = null; // No aplica para solicitudes
        this.actualmenteActiva = false; // No aplica directamente
    }
}