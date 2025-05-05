package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("adminapp")
public class ProductoController {


    @GetMapping("/carga-masiva")
    public String cargarMasivaProductos(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/adminapp/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);

        return "producto/carga-masiva";
    }

    @GetMapping("/borrado-masivo")
    public String eliminacionMasivaProductos(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/adminapp/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);

        return "producto/eliminacion-masiva";
    }


}


