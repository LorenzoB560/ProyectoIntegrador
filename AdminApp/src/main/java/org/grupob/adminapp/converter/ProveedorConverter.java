package org.grupob.adminapp.converter; // O org.grupob.adminapp.converter.maestras

import org.grupob.adminapp.dto.ProveedorDTO; // Importa tu ProveedorDTO de AdminApp
import org.grupob.comun.entity.maestras.Proveedor;    // Importa tu entidad Proveedor de comun
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component // Para que Spring lo detecte y puedas inyectarlo
public class ProveedorConverter {

    private final ModelMapper modelMapper;

    // Inyecta ModelMapper (asegúrate de que esté configurado como Bean en algún sitio)
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
