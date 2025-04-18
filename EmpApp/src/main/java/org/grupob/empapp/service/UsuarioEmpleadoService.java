package org.grupob.empapp.service;

import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.entity.UsuarioEmpleado;

public interface UsuarioEmpleadoService {
    public LoginUsuarioEmpleadoDTO login(LoginUsuarioEmpleadoDTO dto);
}
