package org.grupob.adminapp.converter;

import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.dto.AltaEmpleadoDTO;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.adminapp.dto.ModificacionEmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpleadoConverterAdmin {

    private final ModelMapper modelMapper;

    public Empleado convertirAEntidad(AltaEmpleadoDTO altaEmpleadoDTO){
        return modelMapper.map(altaEmpleadoDTO, Empleado.class);
    }

    public EmpleadoDTO convertToDto(Empleado empleado) {
        return modelMapper.map(empleado, EmpleadoDTO.class);
    }

    public Empleado convertirAEntidadDesdeModificacion(ModificacionEmpleadoDTO modificacionEmpleadoDTO){
        return modelMapper.map(modificacionEmpleadoDTO, Empleado.class);
    }
}
