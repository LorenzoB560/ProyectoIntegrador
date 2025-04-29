package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
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
    public String listadoDepartamentoVista(@PathVariable String id, Model modelo, HttpServletRequest request, HttpSession sesion){

        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");
        modelo.addAttribute("dto", dto);
        return "listados/detalle-vista-dep";
    }


}
