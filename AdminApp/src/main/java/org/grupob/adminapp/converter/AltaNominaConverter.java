package org.grupob.adminapp.converter;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.comun.entity.Nomina;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

@Component
public class AltaNominaConverter {
    private final ModelMapper modelMapper;

    public AltaNominaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Nomina altaNominaDTOConvierteAEntidad(AltaNominaDTO altaNominaDTO){
        // Mapear propiedades básicas
        return modelMapper.map(altaNominaDTO, Nomina.class);
    }
}
