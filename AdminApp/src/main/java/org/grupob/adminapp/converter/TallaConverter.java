package org.grupob.adminapp.converter;

import org.grupob.adminapp.dto.TallaDTO;
import org.grupob.comun.entity.maestras.Talla;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TallaConverter {

    private final ModelMapper modelMapper;


    public TallaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public TallaDTO entityToDto(Talla entidad) {
        if (entidad == null) {
            return null;
        }
        return modelMapper.map(entidad, TallaDTO.class);
    }
}
