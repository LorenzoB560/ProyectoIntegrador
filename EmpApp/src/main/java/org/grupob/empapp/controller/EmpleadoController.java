package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("empleado")
public class EmpleadoController {

    @GetMapping("lista")
    public String listadovista() {
        return "listados/listado-vista-emp";
    }
    @GetMapping("detalle/{id}")
    public String listadoEmpleadoVista(@PathVariable String id, Model modelo, HttpSession sesion){

        modelo.addAttribute("dto", sesion.getAttribute("usuarioLogeado"));

        return "listados/detalle-vista-emp";
    }
}
