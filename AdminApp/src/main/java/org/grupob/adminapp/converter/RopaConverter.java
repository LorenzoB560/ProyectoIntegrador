package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.RopaDTO;
import org.grupob.comun.entity.Mueble;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RopaConverter {

    private final ModelMapper modelMapper;

    public RopaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RopaDTO convertirADto(Mueble entidad) {
        return modelMapper.map(entidad, RopaDTO.class);
    }

    public Mueble convertirAEntidad(RopaDTO dto) {
        return modelMapper.map(dto, Mueble.class);
    }
}
