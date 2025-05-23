package org.grupob.empapp.converter;

import org.grupob.empapp.dto.ProveedorDTO;
import org.grupob.comun.entity.maestras.Proveedor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProveedorConverter {

    private final ModelMapper modelMapper;

    public ProveedorConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public ProveedorDTO entityToDTO(Proveedor entidad) {
        if (entidad == null) {
            return null;
        }
        return modelMapper.map(entidad, ProveedorDTO.class);
    }
}
