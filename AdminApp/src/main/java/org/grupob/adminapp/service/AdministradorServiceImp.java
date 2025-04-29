package org.grupob.adminapp.service;

import org.grupob.adminapp.converter.AdministradorConverter;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.comun.entity.Administrador;
import org.grupob.comun.exception.CredencialesInvalidasException;
import org.grupob.comun.exception.UsuarioNoEncontradoException;
import org.grupob.adminapp.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministradorServiceImp implements AdministradorService {
    private final AdministradorRepository adminRepo;
    private final AdministradorConverter adminConverter;

    public AdministradorServiceImp(AdministradorRepository adminRepo, AdministradorConverter adminConverter) {
        this.adminRepo = adminRepo;
        this.adminConverter = adminConverter;
    }

    public Administrador devuelveAdministradorPorUsuario(String usuario) {
        Optional<Administrador> adminOpt = adminRepo.findAdministradorByUsuario(usuario);

        if (adminOpt.isPresent()) {
            return adminOpt.get();
        }
        throw new UsuarioNoEncontradoException("No existe un usuario con ese correo");
    }

    public LoginAdministradorDTO  comprobarCredenciales(LoginAdministradorDTO adminDTO) {
        Administrador admin = adminRepo.findAdministradorByUsuario(adminDTO.getUsuario())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        if (!adminDTO.getClave().equals(admin.getClave())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        // Aumentamos número de accesos
        admin = aumentarNumeroAccesos(admin);

        return adminConverter.convertirADTO(admin);
    }

    public Administrador aumentarNumeroAccesos(Administrador admin) {
        admin.setNumeroAccesos(admin.getNumeroAccesos() + 1);
        return adminRepo.save(admin);
    }

}
