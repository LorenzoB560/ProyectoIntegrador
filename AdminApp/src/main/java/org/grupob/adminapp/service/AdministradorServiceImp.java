package org.grupob.adminapp.service;

import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.entity.Administrador;
import org.grupob.adminapp.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministradorServiceImp implements AdministradorService {
    private final AdministradorRepository adminRepo;

    public AdministradorServiceImp(AdministradorRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Administrador devuelveAdministradorPorCorreo(String correo) {
        Optional<Administrador> adminOpt = adminRepo.findByCorreo(correo);

        if (adminOpt.isPresent()) {
            return adminOpt.get();
        }
        throw new RuntimeException("No existe un usuario con ese correo");
    }

    public Boolean comprobarCredenciales(LoginAdministradorDTO adminDTO) {
        Optional<Administrador> optionalAdmin = adminRepo.findByCorreo(adminDTO.getCorreo());
        if (optionalAdmin.isEmpty()) {
            return false; // //TODO Cambiar luego por la excepcion correspondiente
        }

        return adminDTO.getClave().equals(optionalAdmin.get().getClave());
    }

   /* public String devolverClavePorCorreo(String correo) {
        return devuelveAdministradorPorCorreo(correo).getClave();
    }

    public Boolean comprobarClave(String correo, String clave) {
        return null;
    }

    public Boolean comprobarCorreo(String correo) {
        return adminRepo.existsByCorreo(correo);
    }*/
}
