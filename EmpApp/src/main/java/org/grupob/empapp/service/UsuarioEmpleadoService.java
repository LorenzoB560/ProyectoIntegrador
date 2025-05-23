package org.grupob.empapp.service;

import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.entity.UsuarioEmpleado;

public interface UsuarioEmpleadoService {
    LoginUsuarioEmpleadoDTO devuelveUsuarioEmpPorUsuario(String usuario);
    Boolean validarEmail(String correo);
     Boolean validarCredenciales(LoginUsuarioEmpleadoDTO dto);
     void actualizarEstadisticasAcceso(LoginUsuarioEmpleadoDTO dto);
     int manejarIntentoFallido(UsuarioEmpleado usuario);

}
