package org.grupob.empapp.converter;

import org.grupob.empapp.dto.DepartamentoDTO;
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
        DepartamentoDTO deparetamentoDto = modelMapper.map(departamento, DepartamentoDTO.class);
//        posDto.setSubmissionDate(post.getSubmissionDate(),
//                userService.getCurrentUser().getPreference().getTimezone());
        return deparetamentoDto;
    }
}
