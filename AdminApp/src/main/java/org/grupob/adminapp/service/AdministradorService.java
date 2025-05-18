package org.grupob.adminapp.service;

import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.entity.Administrador;

public interface AdministradorService {

    Administrador devuelveAdministradorPorUsuario(String usuario);
    LoginAdministradorDTO comprobarCredenciales(LoginAdministradorDTO adminDTO);
    Administrador aumentarNumeroAccesos(Administrador admin);
}
