package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.converter.AdministradorConverter;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.adminapp.service.AdministradorServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("adminapp")
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


    @GetMapping("area-personal")
    public String areaPersonal(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if (adminDTO == null) {
            return "redirect:/login"; // protección ante acceso directo sin login
        }

        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "redirect:/empleado/lista";
    }

    // Endpoint para desconectar
    @GetMapping("/desconectar")
    public String desconectarUsuario(HttpSession sesion) {

        sesion.removeAttribute("adminLogueado");
        // Redirige al inicio
        return "redirect:/login";
    }


}


