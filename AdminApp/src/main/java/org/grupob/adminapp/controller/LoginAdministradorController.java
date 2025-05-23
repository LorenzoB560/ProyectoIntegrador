package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginAdministradorController {

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


