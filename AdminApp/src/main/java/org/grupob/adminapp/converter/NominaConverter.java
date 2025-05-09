package org.grupob.adminapp.converter;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.LineaNomina;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.repository.ConceptoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    public Nomina convierteAEntidad(AltaNominaDTO altaNominaDTO){
        // Mapear propiedades básicas
        return modelMapper.map(altaNominaDTO, Nomina.class);
    }

    public NominaDTO convierteADTO(Nomina nomina){
        NominaDTO nominaDTO = modelMapper.map(nomina, NominaDTO.class);
        Optional<Empleado> empleado = empleadoRepository.findById(nomina.getEmpleado().getId());
        empleado.ifPresent(value -> nominaDTO.setNombre(value.getNombre() + " " + value.getApellido()));
        return nominaDTO;
    }
}