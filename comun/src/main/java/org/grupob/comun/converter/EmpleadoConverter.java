package org.grupob.comun.converter;

import lombok.RequiredArgsConstructor;
import org.grupob.comun.dto.EmpleadoNominaDTO;
import org.grupob.comun.entity.Empleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpleadoConverter {
    private final ModelMapper modelMapper;

    public EmpleadoNominaDTO convierteAEmpleadoNominaDTO(Empleado empleadoNominaDTO) {
        return modelMapper.map(empleadoNominaDTO, EmpleadoNominaDTO.class);
    }
}
