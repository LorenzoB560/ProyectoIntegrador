package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.masiva.MuebleCargaDTO;
import org.grupob.comun.entity.Mueble;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MuebleConverter {
    private final ModelMapper modelMapper;

    public MuebleConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public MuebleCargaDTO convertirADto(Mueble mueble) {
        return modelMapper.map(mueble, MuebleCargaDTO.class);
    }

    public Mueble convertirAEntidad(MuebleCargaDTO dto) {
        return modelMapper.map(dto, Mueble.class);
    }
}
