package org.grupob.empapp.converter;

import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LoginUsuarioEmpleadoConverter {
    ModelMapper modelMapper;


    public LoginUsuarioEmpleadoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LoginUsuarioEmpleadoDTO convertirADTO(UsuarioEmpleado usuarioEmp) {
        return modelMapper.map(usuarioEmp, LoginUsuarioEmpleadoDTO.class);
    }
}
