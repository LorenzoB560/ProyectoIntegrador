package org.grupob.adminapp.converter;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.comun.entity.LineaNomina;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.repository.ConceptoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NominaConverter {

    private final ConceptoRepository conceptoRepository;
    private final ModelMapper modelMapper;

    public NominaConverter(ConceptoRepository conceptoRepository, ModelMapper modelMapper) {
        this.conceptoRepository = conceptoRepository;
        this.modelMapper = modelMapper;
    }

    public Nomina convierteAEntidad(AltaNominaDTO altaNominaDTO){
        // Mapear propiedades básicas


        return modelMapper.map(altaNominaDTO, Nomina.class);
    }
}