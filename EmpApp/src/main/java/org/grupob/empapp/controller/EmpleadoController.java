package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.service.CookieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

        if(dto==null){
            return "redirect:/empapp/login";
        }

        modelo.addAttribute("dto", dto);

        int contador = cookieService.obtenerInicios(usuariosCookie,ultimoUsuario);

        modelo.addAttribute("contador", contador);
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        modelo.addAttribute("ultimaPagina", ultimaPagina);

        return "listados/detalle-vista-emp";
    }
}
