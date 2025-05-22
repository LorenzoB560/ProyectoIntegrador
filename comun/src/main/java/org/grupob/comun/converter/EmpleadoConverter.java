package org.grupob.comun.converter;

import lombok.RequiredArgsConstructor;
import org.grupob.comun.dto.EmpleadoNominaDTO;
import org.grupob.comun.dto.PeriodoDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.auxiliar.Periodo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpleadoConverter {
    private final ModelMapper modelMapper;

    public EmpleadoNominaDTO convierteAEmpleadoNominaDTO(Empleado empleado) {
        EmpleadoNominaDTO dto = modelMapper.map(empleado, EmpleadoNominaDTO.class);

        if (empleado.getPeriodo() != null) {
            Periodo periodo = empleado.getPeriodo();
            dto.setPeriodoContratacion(new PeriodoDTO(periodo.getFechaInicio(), periodo.getFechaFin()));
        }

        return dto;
    }
}
