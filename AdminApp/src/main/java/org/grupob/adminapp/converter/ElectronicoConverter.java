package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.ElectronicoDTO;
import org.grupob.comun.entity.Electronico;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ElectronicoConverter {

    private final ModelMapper modelMapper;

    public ElectronicoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ElectronicoDTO convertirADto(Electronico entidad) {
        return modelMapper.map(entidad, ElectronicoDTO.class);
    }

    public Electronico convertirAEntidad(ElectronicoDTO dto) {
        return modelMapper.map(dto, Electronico.class);
    }
}
