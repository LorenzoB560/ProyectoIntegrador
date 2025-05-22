package org.grupob.empapp.converter;

import org.grupob.comun.entity.auxiliar.Periodo; // Tu entidad Periodo
import org.grupob.empapp.dto.PeriodoDTO;         // Tu PeriodoDTO
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PeriodoConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public PeriodoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PeriodoDTO toDto(Periodo periodo) {
        if (periodo == null) {
            return null;
        }
        return modelMapper.map(periodo, PeriodoDTO.class);
    }

    public List<PeriodoDTO> toDtoList(List<Periodo> periodos) {
        if (periodos == null) {
            return null;
        }
        return periodos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}