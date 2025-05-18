package org.grupob.adminapp.converter; // O simplemente org.grupob.adminapp.converter

import org.grupob.adminapp.dto.TallaDTO; // Importa tu TallaDTO
import org.grupob.comun.entity.maestras.Talla;    // Importa tu entidad Talla de comun
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component // Para que Spring lo detecte y puedas inyectarlo
public class TallaConverter {

    private final ModelMapper modelMapper;

    // Inyecta ModelMapper
    public TallaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public TallaDTO entityToDto(Talla entidad) {
        if (entidad == null) {
            return null;
        }
        // ModelMapper debería manejar esto directamente si los nombres coinciden (id, nombre)
        return modelMapper.map(entidad, TallaDTO.class);
    }
}
