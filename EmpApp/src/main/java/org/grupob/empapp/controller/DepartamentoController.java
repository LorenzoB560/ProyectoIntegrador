package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.service.CookieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("departamento")
public class DepartamentoController {
    private final CookieService cookieService;

    public DepartamentoController(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @GetMapping("lista1")
    public String listadovista(){
        return "departamentos/listado-vista";
    }
    @GetMapping("detalle/{id}")
    public String listadoDepartamentoVista(@PathVariable String id,
                                           Model modelo,
                                           @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                           HttpServletRequest request){

        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        if(dto==null){
            return "redirect:/empapp/login";
        }

        modelo.addAttribute("dto", dto);

        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        modelo.addAttribute("ultimaPagina", ultimaPagina);
        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
        modelo.addAttribute("contador", contador);
        return "listados/detalle-vista-dep";
    }


}
