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
        Nomina nomina = modelMapper.map(altaNominaDTO, Nomina.class);

//        // Inicializar el conjunto si es null
//        if (nomina.getLineaNominas() == null) {
//            nomina.setLineaNominas(new HashSet<>());
//        }

//        // Mapear líneas de nómina
//        if (altaNominaDTO.getLineaNominas() != null && !altaNominaDTO.getLineaNominas().isEmpty()) {
//            Set<LineaNomina> lineas = altaNominaDTO.getLineaNominas().stream()
//                    .filter(lineaDTO -> lineaDTO.getIdConcepto() != null) // Asegurar que idConcepto no sea null
//                    .map(lineaDTO -> {
//                        Concepto concepto = conceptoRepository.findById(lineaDTO.getIdConcepto())
//                                .orElseThrow(() -> new RuntimeException("Concepto no encontrado: " + lineaDTO.getIdConcepto()));
//
//                        LineaNomina linea = new LineaNomina();
//                        linea.setConcepto(concepto);
//                        linea.setCantidad(lineaDTO.getCantidad());
//                        linea.setNomina(nomina); // Importante: establecer la relación bidireccional
//
//                        return linea;
//                    })
//                    .collect(Collectors.toSet());
//
//            nomina.setLineaNominas(lineas);
//        }

        return nomina;
    }
}