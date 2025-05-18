package org.grupob.empapp.converter; // O simplemente org.grupob.adminapp.converter

import org.grupob.empapp.dto.TallaDTO;
import org.grupob.comun.entity.maestras.Talla;
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
