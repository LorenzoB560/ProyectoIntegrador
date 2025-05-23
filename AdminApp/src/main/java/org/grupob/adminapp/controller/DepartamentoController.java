package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("departamento")
public class DepartamentoController {

    @GetMapping("lista1")
    public String listadovista(){
        return "departamentos/listado-vista";
    }
    @GetMapping("detalle/{id}")
    public String listadoDepartamentoVista(@PathVariable String id, Model modelo, HttpSession sesion){
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/adminapp/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "listados/detalle-vista-dep";
    }

}
