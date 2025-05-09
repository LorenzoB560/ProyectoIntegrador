package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.masiva.ElectronicoCargaDTO;
import org.grupob.comun.entity.Electronico;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ElectronicoConverter {

    private final ModelMapper modelMapper;

    public ElectronicoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ElectronicoCargaDTO convertirADto(Electronico entidad) {
        return modelMapper.map(entidad, ElectronicoCargaDTO.class);
    }

    public Electronico convertirAEntidad(ElectronicoCargaDTO dto) {
        return modelMapper.map(dto, Electronico.class);
    }
}
