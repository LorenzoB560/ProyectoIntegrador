package org.grupob.adminapp.service;

import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.entity.Administrador;

public interface AdministradorService {

    Administrador devuelveAdministradorPorCorreo(String correo);
    Boolean comprobarCredenciales(LoginAdministradorDTO adminDTO);
    Administrador aumentarNumAccesos(Administrador admin);

//    String devolverClavePorCorreo(String correo);
   /* Boolean comprobarCorreo(String correo);
    Boolean comprobarClave(String correo, String clave);*/
}
