package org.grupob.adminapp.service;

import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.entity.Administrador;

public interface AdministradorService {

    Administrador devuelveAdministradorPorCorreo(String correo);
    LoginAdministradorDTO comprobarCredenciales(LoginAdministradorDTO adminDTO);
    Administrador aumentarNumeroAccesos(Administrador admin);
}
