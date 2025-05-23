package org.grupob.empapp.converter;

import org.grupob.comun.entity.Colaboracion;
import org.grupob.comun.entity.Empleado;
import org.grupob.empapp.dto.ColaboracionEstablecidaDTO;
import org.grupob.empapp.dto.PeriodoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ColaboracionesConverter {

    private final ModelMapper modelMapper;
    private final PeriodoConverter periodoConverter;

    @Autowired
    public ColaboracionesConverter(ModelMapper modelMapper, PeriodoConverter periodoConverter) {
        this.modelMapper = modelMapper;
        this.periodoConverter = periodoConverter;
    }

    // Este método asume que el DTO ColaboracionEstablecidaDTO tiene campos que coinciden
    // con Colaboracion, o que ModelMapper está configurado para manejarlos.
    // La lógica principal se hace en el método toDto más completo.
    public ColaboracionEstablecidaDTO convertirADTOBasico(Colaboracion colaboracion) {
        if (colaboracion == null) return null;
        return modelMapper.map(colaboracion, ColaboracionEstablecidaDTO.class);
    }

    public ColaboracionEstablecidaDTO toDto(Colaboracion colaboracion, UUID idEmpleadoActual) {
        if (colaboracion == null) {
            return null;
        }

        Empleado otroEmpleado;
        if (colaboracion.getEmisor() == null || colaboracion.getReceptor() == null) {
            throw new IllegalStateException("La colaboración con ID " + colaboracion.getId() + " tiene un emisor o receptor nulo.");
        }

        if (colaboracion.getEmisor().getId().equals(idEmpleadoActual)) {
            otroEmpleado = colaboracion.getReceptor();
        } else if (colaboracion.getReceptor().getId().equals(idEmpleadoActual)) {
            otroEmpleado = colaboracion.getEmisor();
        } else {
            throw new IllegalArgumentException("La colaboración con ID " + colaboracion.getId() +
                    " no pertenece al empleado con ID " + idEmpleadoActual);
        }

        ColaboracionEstablecidaDTO dto = new ColaboracionEstablecidaDTO();
        // Mapear campos simples si ModelMapper no los toma por defecto o para ser explícito
        dto.setIdColaboracion(colaboracion.getId());
        dto.setFechaCreacionColaboracion(colaboracion.getFechaCreacion());

        if (otroEmpleado != null) {
            dto.setOtroEmpleadoNombreCompleto(otroEmpleado.getNombre() + " " + otroEmpleado.getApellido());
            dto.setOtroEmpleadoId(otroEmpleado.getId());
        }

        // Usar PeriodoConverter para la lista de periodos y luego ordenar
        List<PeriodoDTO> periodosDto = periodoConverter.toDtoList(colaboracion.getPeriodos());
        if (periodosDto != null) {
            periodosDto.sort((p1, p2) -> {
                if (p1 == null || p1.getFechaInicio() == null) return 1;
                if (p2 == null || p2.getFechaInicio() == null) return -1;
                return p2.getFechaInicio().compareTo(p1.getFechaInicio()); // Descendente
            });
        }
        dto.setPeriodos(periodosDto != null ? periodosDto : new ArrayList<>());
        dto.setActualmenteActiva(colaboracion.getPeriodoActivo().isPresent());

        return dto;
    }

    public List<ColaboracionEstablecidaDTO> toDtoList(List<Colaboracion> colaboraciones, UUID idEmpleadoActual) {
        if (colaboraciones == null || colaboraciones.isEmpty()) {
            return Collections.emptyList();
        }
        List<ColaboracionEstablecidaDTO> dtos = colaboraciones.stream()
                .map(colab -> toDto(colab, idEmpleadoActual))
                .collect(Collectors.toList());

        // Ordenar la lista final de DTOs
        dtos.sort((c1, c2) -> {
            if (c1.isActualmenteActiva() && !c2.isActualmenteActiva()) return -1;
            if (!c1.isActualmenteActiva() && c2.isActualmenteActiva()) return 1;
            if (c1.getPeriodos() != null && !c1.getPeriodos().isEmpty() &&
                    c2.getPeriodos() != null && !c2.getPeriodos().isEmpty() &&
                    c1.getPeriodos().get(0) != null && c1.getPeriodos().get(0).getFechaInicio() != null &&
                    c2.getPeriodos().get(0) != null && c2.getPeriodos().get(0).getFechaInicio() != null) {
                return c2.getPeriodos().get(0).getFechaInicio().compareTo(c1.getPeriodos().get(0).getFechaInicio());
            }
            if (c1.getFechaCreacionColaboracion() != null && c2.getFechaCreacionColaboracion() != null) {
                return c2.getFechaCreacionColaboracion().compareTo(c1.getFechaCreacionColaboracion());
            }
            return 0;
        });
        return dtos;
    }
}