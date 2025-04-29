package org.grupob.empapp.converter;

import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RegistroUsuarioEmpleadoConverter {
    private ModelMapper modelMapper;

    public RegistroUsuarioEmpleadoConverter() {
        this.modelMapper = new ModelMapper();
    }

    public UsuarioEmpleado convertirAEntidad(RegistroUsuarioEmpleadoDTO usuario){
        return modelMapper.map(usuario, UsuarioEmpleado.class);
    }

    /*public UsuarioEmpleado convertirAEntidad(LoginUsuarioEmpleadoDTO dto) {
        return modelMapper.map(dto, UsuarioEmpleado.class);
    }*/
}
