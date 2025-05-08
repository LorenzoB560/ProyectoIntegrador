package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.masiva.RopaCargaDTO;
import org.grupob.comun.entity.Mueble;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RopaConverter {

    private final ModelMapper modelMapper;

    public RopaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RopaCargaDTO convertirADto(Mueble entidad) {
        return modelMapper.map(entidad, RopaCargaDTO.class);
    }

    public Mueble convertirAEntidad(RopaCargaDTO dto) {
        return modelMapper.map(dto, Mueble.class);
    }
}
