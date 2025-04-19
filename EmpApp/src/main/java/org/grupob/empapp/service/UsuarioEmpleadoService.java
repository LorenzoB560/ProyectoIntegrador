package org.grupob.empapp.service;

import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.entity.UsuarioEmpleado;

public interface UsuarioEmpleadoService {
     Boolean validarEmail(String correo);
     Boolean validarCredenciales(LoginUsuarioEmpleadoDTO dto);
     void actualizarEstadisticasAcceso(UsuarioEmpleado usuario);
    void manejarIntentoFallido(UsuarioEmpleado usuario);

}
