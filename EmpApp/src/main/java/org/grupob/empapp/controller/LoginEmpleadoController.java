package org.grupob.empapp.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.service.CookieService;
import org.grupob.empapp.service.UsuarioEmpleadoServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("empapp")
public class LoginEmpleadoController {



    private final UsuarioEmpleadoServiceImp usuarioService;
    private final CookieService cookieService;

    public LoginEmpleadoController(UsuarioEmpleadoServiceImp usuarioService,
                          CookieService cookieService) {
        this.usuarioService = usuarioService;
        this.cookieService = cookieService;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpServletRequest request,
                               @CookieValue(name = "usuario", required = false) String usuariosCookie) {
        // Obtener historial de logins desde cookies

        String historialCookie =(CookieService.cookieValida(usuariosCookie))
                ? CookieService.deserializar(usuariosCookie).toString() : null;

//        String historialCookie = CookieService.obtenerValorCookie(request, "historialLogins");
        model.addAttribute("historial", CookieService.deserializar(historialCookie));
        model.addAttribute("dto", new LoginUsuarioEmpleadoDTO());
        return "login/pedir-usuario";
    }

    @PostMapping("/procesar-usuario")
    public String procesarEmail(@ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                BindingResult result,
                                HttpServletResponse response,
                                HttpSession session) {

        try {
            LoginUsuarioEmpleadoDTO usuario = usuarioService.validarEmail(dto.getCorreo(), response);
            session.setAttribute("usuarioTemp", usuario);
            return "redirect:/empapp/clave";
        } catch (RuntimeException e) {
            result.rejectValue("correo", "error.email", e.getMessage());
            return "login/pedir-usuario";
        }
    }

    @GetMapping("/clave")
    public String mostrarContrasena(HttpSession session, Model model) {
        LoginUsuarioEmpleadoDTO usuario = (LoginUsuarioEmpleadoDTO) session.getAttribute("usuarioTemp");
        if(usuario == null) return "redirect:/empapp/login";

        model.addAttribute("dto", new LoginUsuarioEmpleadoDTO());
        return "login/pedir-clave";
    }

    @PostMapping("/procesar-clave")
    public String procesarContrasena(@ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                     BindingResult result,
                                     HttpSession session,
                                     HttpServletResponse response) {

        LoginUsuarioEmpleadoDTO usuario = (LoginUsuarioEmpleadoDTO) session.getAttribute("usuarioTemp");
        dto.setCorreo(usuario.getCorreo());

        try {
            LoginUsuarioEmpleadoDTO usuarioAutenticado = usuarioService.validarCredenciales(dto, response);
            session.removeAttribute("usuarioTemp");
            return "redirect:/empapp/area-personal";
        } catch (RuntimeException e) {
            result.reject("error.contrasena", e.getMessage());
            return "login/pedir-clave";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpServletResponse response,
                               @CookieValue(name = "sesionActiva") String usuarioId) {
        cookieService.cerrarSesion(response, usuarioId);
        return "redirect:/empapp/login";
    }

    @GetMapping("/area-personal")
    public String mostrarDashboard(Model model, HttpServletRequest request) {
        String usuario= CookieService.obtenerValorCookie(request, "sesionActiva");
        // Lógica para obtener datos del usuario y mostrarlos
        return "area-personal";
    }


    @GetMapping("/devuelve-clave")
    @ResponseBody
    public String devuelveClave(@RequestParam(required = false) String usuario) {
        return null;
    }
}




