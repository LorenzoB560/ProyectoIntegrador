package org.grupob.empapp.converter;

import org.grupob.comun.dto.DepartamentoDTO;
import org.grupob.comun.entity.Departamento;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoConverter {
    ModelMapper modelMapper;


    public DepartamentoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DepartamentoDTO convertToDto(Departamento departamento) {
        return modelMapper.map(departamento, DepartamentoDTO.class);
    }
}
