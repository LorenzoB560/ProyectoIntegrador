package org.grupob.adminapp.controller;

import jakarta.validation.Valid;
import org.grupob.adminapp.converter.AdministradorConverter;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.entity.Administrador;
import org.grupob.adminapp.service.AdministradorServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("adminapp")
public class LoginAdministradorController {

    private final AdministradorServiceImp adminServicio;
    private final AdministradorConverter adminConverter;

    public LoginAdministradorController(AdministradorServiceImp adminServicio, AdministradorConverter adminConverter) {
        this.adminServicio = adminServicio;
        this.adminConverter = adminConverter;
    }

    // Endpoint inicial: /correo (ingresar nombre de usuario)
    @GetMapping("login")
    public String login(@ModelAttribute("loginAdminDTO") LoginAdministradorDTO loginAdminDTO) {

        return "login";
    }


    @PostMapping("login")
    public String comprobrarCredenciales(Model modelo,
                                         @Valid @ModelAttribute("loginAdminDTO") LoginAdministradorDTO loginAdminDTO,
                                         BindingResult resultadoValidacion) {

        if (resultadoValidacion.hasErrors()) { //si hay errores
            return "login";
        }

        if (adminServicio.comprobarCredenciales(loginAdminDTO)) {
            Administrador admin = adminServicio.devuelveAdministradorPorCorreo(loginAdminDTO.getCorreo());
            admin = adminServicio.aumentarNumAccesos(admin);
            loginAdminDTO = adminConverter.convertirADTO(admin);
            modelo.addAttribute("loginAdminDTO", loginAdminDTO);

            return "area-personal";
        }
        modelo.addAttribute("ErrorCredenciales", "Usuario/Clave incorrecta");
        return "login";

    }

}


