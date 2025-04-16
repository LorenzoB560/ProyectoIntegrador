package org.grupob.empapp.converter;

import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.entity.Empleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoConverter {
    ModelMapper modelMapper;


    public EmpleadoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Empleado convertirAEntidad(AltaEmpleadoDTO altaEmpleadoDTO){
        return modelMapper.map(altaEmpleadoDTO, Empleado.class);
    }
}
