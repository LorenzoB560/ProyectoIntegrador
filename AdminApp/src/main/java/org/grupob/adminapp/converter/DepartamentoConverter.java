package org.grupob.adminapp.converter;

import org.grupob.comun.entity.Departamento;
import org.grupob.adminapp.dto.DepartamentoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoConverter {
    ModelMapper modelMapper;


    public DepartamentoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DepartamentoDTO convertToDto(Departamento departamento) {
        DepartamentoDTO deparetamentoDto = modelMapper.map(departamento, DepartamentoDTO.class);
//        posDto.setSubmissionDate(post.getSubmissionDate(),
//                userService.getCurrentUser().getPreference().getTimezone());
        return deparetamentoDto;
    }
}
