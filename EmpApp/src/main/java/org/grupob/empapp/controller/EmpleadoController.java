package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.service.CookieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("empleado")
public class EmpleadoController {


    private final CookieService cookieService;

    public EmpleadoController(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @GetMapping("lista")
    public String listadovista() {
        return "listados/listado-vista-emp";
    }
    @GetMapping("detalle/{id}")
    public String listadoEmpleadoVista(@PathVariable String id, Model modelo,
                                       HttpServletRequest request,
                                       HttpSession sesion,
                                       @CookieValue(name = "usuario", required = false) String usuariosCookie){

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        modelo.addAttribute("dto", dto);
        Map<String, Integer> usuariosAutenticados = (cookieService.validar(usuariosCookie))
                ? cookieService.deserializar(usuariosCookie) : null;
        int contador = usuariosAutenticados.getOrDefault(ultimoUsuario, 1);
        return "listados/detalle-vista-emp";
    }
}
