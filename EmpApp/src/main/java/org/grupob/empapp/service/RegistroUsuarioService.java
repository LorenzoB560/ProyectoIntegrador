package org.grupob.empapp.service;

import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;

public interface RegistroUsuarioService {
    void guardarUsuario(RegistroUsuarioEmpleadoDTO usuario);
    void usuarioExiste(RegistroUsuarioEmpleadoDTO registroUsuarioEmpleadoDTO);
}
