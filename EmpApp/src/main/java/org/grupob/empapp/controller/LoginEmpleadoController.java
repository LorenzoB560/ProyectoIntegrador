package org.grupob.empapp.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.service.CookieService;
import org.grupob.empapp.service.UsuarioEmpleadoServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("empapp")
public class LoginEmpleadoController {

    private final UsuarioEmpleadoServiceImp usuarioEmpleadoServicio;
    private final CookieService gestionCookies;

    public LoginEmpleadoController(UsuarioEmpleadoServiceImp usuarioEmpleadoServicio, CookieService gestionCookies) {
        this.usuarioEmpleadoServicio = usuarioEmpleadoServicio;
        this.gestionCookies = gestionCookies;
    }


    // Primera ventana: se solicita el correo
    @GetMapping
    public String mostrarVentanaCorreo() {
        return "login/pedircorreo";
    }

    @PostMapping("/correo")
    public String procesarCorreo(@RequestParam("correo") String correo,
                                 HttpServletResponse response,
                                 Model model) {
        try {
            // Validar existencia de usuario y guardar correo en cookie
            LoginUsuarioEmpleadoDTO dto = new LoginUsuarioEmpleadoDTO();
            dto.setCorreo(correo);

            LoginUsuarioEmpleadoDTO resultado = usuarioEmpleadoServicio.login(dto); // lanza excepción si no existe

            // Si no hay excepción, el correo existe. Guardamos en cookie.
            GestionCookies.guardarCorreoEnCookie(response, correo);
            return "redirect:/login/clave";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login/ventana-correo";
        }
    }

    // Segunda ventana: se solicita la clave
    @GetMapping("/clave")
    public String mostrarVentanaClave(HttpServletRequest request, Model model) {
        String correo = GestionCookies.obtenerCorreoDeCookie(request);

        if (correo == null) {
            return "redirect:/login";
        }

        model.addAttribute("correo", correo);
        return "login/ventana-clave";
    }

    @PostMapping("/clave")
    public String procesarClave(@RequestParam("clave") String clave,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
        String correo = GestionCookies.obtenerCorreoDeCookie(request);

        if (correo == null) {
            return "redirect:/login";
        }

        LoginUsuarioEmpleadoDTO dto = new LoginUsuarioEmpleadoDTO();
        dto.setCorreo(correo);
        dto.setClave(clave);

        LoginUsuarioEmpleadoDTO resultado = usuarioEmpleadoServicio.login(dto);

        if (resultado.isBloqueado()) {
            model.addAttribute("bloqueado", true);
            model.addAttribute("motivoBloqueo", resultado.getMotivoBloqueo());
            model.addAttribute("mensajeBloqueo", resultado.getMensajeBloqueo());
            return "login/ventana-clave";
        }

        // Login exitoso
        GestionCookies.borrarCookieCorreo(response);
        model.addAttribute("usuario", resultado);
        return "login/ventana-personal";
    }
    // Endpoint para desconectar
    @GetMapping("/desconectar")
    public String desconectarUsuario(HttpServletResponse response) {
        // Elimina la cookie "estado"
        Cookie estadoCookie = new Cookie("estado", "");
        estadoCookie.setPath("/");
        estadoCookie.setMaxAge(0);
        response.addCookie(estadoCookie);

        // Elimina la cookie "ultimoUsuario"
        Cookie ultimoUsuarioCookie = new Cookie("ultimoUsuario", "");
        ultimoUsuarioCookie.setPath("/");
        ultimoUsuarioCookie.setMaxAge(0);
        response.addCookie(ultimoUsuarioCookie);

        // Redirige al inicio
        return "redirect:/correo";
    }


    @GetMapping("/devuelve-clave")
    @ResponseBody
    public String devuelveClave(@RequestParam(required = false) String usuario) {
        return null;
    }

}
