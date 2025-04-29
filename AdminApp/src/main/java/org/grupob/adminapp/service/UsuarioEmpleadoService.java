package org.grupob.adminapp.service;

public interface UsuarioEmpleadoService {

    void bloquearEmpleado(String empleadoId, Long motivoId);
    void desbloquearEmpleado(String empleadoId);
}
