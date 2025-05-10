package org.grupob.comun.converter;


import org.grupob.comun.dto.LineaNominaDTO;
import org.grupob.comun.dto.NominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.repository.ConceptoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class NominaConverter {

    private final ConceptoRepository conceptoRepository;
    private final ModelMapper modelMapper;
    private final EmpleadoRepository empleadoRepository;

    public NominaConverter(ConceptoRepository conceptoRepository, ModelMapper modelMapper, EmpleadoRepository empleadoRepository) {
        this.conceptoRepository = conceptoRepository;
        this.modelMapper = modelMapper;
        this.empleadoRepository = empleadoRepository;
    }

    public Nomina nominaDTOConvierteAEntidad(NominaDTO nominaDTO){
        return modelMapper.map(nominaDTO, Nomina.class);
    }


    public NominaDTO convierteANominaDTO(Nomina nomina){
        NominaDTO nominaDTO = modelMapper.map(nomina, NominaDTO.class);
        Optional<Empleado> empleado = empleadoRepository.findById(nomina.getEmpleado().getId());
        empleado.ifPresent(value -> nominaDTO.setNombre(value.getNombre() + " " + value.getApellido()));

        // Ordenar descendetemente las líneas de nómina según la cantidad
        nominaDTO.setLineaNominas(nominaDTO.getLineaNominas().stream()
                .sorted(Comparator.comparing(LineaNominaDTO::getCantidad).reversed())
                .collect(Collectors.toList()));
        return nominaDTO;
    }
}