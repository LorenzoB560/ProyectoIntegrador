package org.grupob.empapp.converter;

import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
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
        LoginUsuarioEmpleadoDTO usuarioEmpDTO = modelMapper.map(usuarioEmp, LoginUsuarioEmpleadoDTO.class);

      /*  // Datos adicionales que no se mapean automáticamente
        if (usuarioEmp.getMotivoBloqueo() != null) {
            usuarioEmpDTO.setBloqueado(true);
            usuarioEmpDTO.setMotivoBloqueo(usuarioEmp.getMotivoBloqueo().getMotivo());
        } else {
            usuarioEmpDTO.setBloqueado(false);
        }*/

        return usuarioEmpDTO;
    }

    //NO es necesario para la funcionalidad del login
    /*public UsuarioEmpleado convertirAEntidad(LoginUsuarioEmpleadoDTO dto) {
        return modelMapper.map(dto, UsuarioEmpleado.class);
    }*/
}
