package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("empleado")
public class EmpleadoController {

    @GetMapping("lista")
    public String listadovista(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if(adminDTO==null){
            return "redirect:/adminapp/login";
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "listados/listado-vista-emp";
    }
    @GetMapping("detalle/{id}")
    public String listadoEmpleadoVista(@PathVariable String id, Model modelo, HttpSession sesion){
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if(adminDTO==null){
            return "redirect:/adminapp/login";
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "listados/detalle-vista-emp";
    }
}
