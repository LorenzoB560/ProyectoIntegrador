package org.grupob.empapp.converter;

import org.grupob.comun.entity.SolicitudColaboracion;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolicitudColaboracionConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public SolicitudColaboracionConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SolicitudColaboracionDTO toDto(SolicitudColaboracion solicitud) {
        if (solicitud == null) {
            return null;
        }
        // Mapeo básico
        SolicitudColaboracionDTO dto = modelMapper.map(solicitud, SolicitudColaboracionDTO.class);

        // Lógica manual para campos que no se mapean directamente o necesitan transformación
        if (solicitud.getSolicitante() != null) {
            dto.setSolicitanteId(solicitud.getSolicitante().getId());
            dto.setSolicitanteNombre(solicitud.getSolicitante().getNombre() + " " + solicitud.getSolicitante().getApellido());
        }
        if (solicitud.getReceptor() != null) {
            dto.setReceptorId(solicitud.getReceptor().getId());
            dto.setReceptorNombre(solicitud.getReceptor().getNombre() + " " + solicitud.getReceptor().getApellido());
        }
        if (solicitud.getEstado() != null) {
            dto.setEstado(solicitud.getEstado().getNombre());
        }
        // ModelMapper debería mapear fechaSolicitud si los nombres son iguales.

        return dto;
    }

    // Sobrecarga para el caso de solicitudes enviadas (solo necesitamos el receptor)
    public SolicitudColaboracionDTO toDtoParaEnviadas(SolicitudColaboracion solicitud) {
        if (solicitud == null) return null;
        return new SolicitudColaboracionDTO(
                solicitud.getId(),
                solicitud.getReceptor() != null ? (solicitud.getReceptor().getNombre() + " " + solicitud.getReceptor().getApellido()) : "N/A",
                solicitud.getFechaSolicitud(),
                solicitud.getEstado() != null ? solicitud.getEstado().getNombre() : "N/A"
        );
    }

    // Sobrecarga para el caso de solicitudes recibidas (necesitamos el solicitante)
    public SolicitudColaboracionDTO toDtoParaRecibidas(SolicitudColaboracion solicitud) {
        if (solicitud == null) return null;
        return new SolicitudColaboracionDTO(
                solicitud.getId(),
                solicitud.getSolicitante() != null ? (solicitud.getSolicitante().getNombre() + " " + solicitud.getSolicitante().getApellido()) : "N/A",
                solicitud.getFechaSolicitud(),
                solicitud.getEstado() != null ? solicitud.getEstado().getNombre() : "N/A",
                solicitud.getSolicitante() != null ? solicitud.getSolicitante().getId() : null
        );
    }
}