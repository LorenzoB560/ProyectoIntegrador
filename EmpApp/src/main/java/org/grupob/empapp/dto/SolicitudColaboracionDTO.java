package org.grupob.empapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudColaboracionDTO {
    private UUID id;
    private UUID solicitanteId;
    private String solicitanteNombre;
    private UUID receptorId;
    private String receptorNombre;
    private LocalDateTime fechaSolicitud;
    private String estado;
    // Constructor para solicitudes enviadas
    public SolicitudColaboracionDTO(UUID id, String receptorNombre, LocalDateTime fechaSolicitud, String estado) {
        this.id = id;
        this.receptorNombre = receptorNombre;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
    }
    // Constructor para solicitudes recibidas
    public SolicitudColaboracionDTO(UUID id, String solicitanteNombre, LocalDateTime fechaSolicitud, String estado, UUID solicitanteId) {
        this.id = id;
        this.solicitanteNombre = solicitanteNombre;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.solicitanteId = solicitanteId; // Necesario para identificar al emisor al aceptar/rechazar
    }


}

