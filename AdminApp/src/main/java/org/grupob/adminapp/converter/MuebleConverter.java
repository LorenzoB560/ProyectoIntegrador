package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.LibroDTO;
import org.grupob.adminapp.dto.MuebleDTO;
import org.grupob.comun.entity.Libro;
import org.grupob.comun.entity.Mueble;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MuebleConverter {
    private final ModelMapper modelMapper;

    public MuebleConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public MuebleDTO convertirADto(Mueble mueble) {
        return modelMapper.map(mueble, MuebleDTO.class);
    }

    public Mueble convertirAEntidad(MuebleDTO dto) {
        return modelMapper.map(dto, Mueble.class);
    }
}
