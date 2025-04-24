package org.grupob.empapp.controller;

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

import java.util.HashMap;
import java.util.Map;

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
    public String mostrarLogin(Model modelo, HttpServletRequest request,
                               HttpServletResponse respuesta,
                               @CookieValue(name = "usuario", required = false) String usuariosCookie,
                               @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto) {
        Map<String, Integer> usuariosAutenticados = (cookieService.validar(usuariosCookie))
                ? cookieService.deserializar(usuariosCookie) : null;
        String estado = cookieService.obtenerValorCookie(request, "estado");
        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");

        if (ultimoUsuario == null || (usuariosAutenticados != null && !usuariosAutenticados.containsKey(ultimoUsuario))) {
            cookieService.crearCookie(respuesta, "estado", "login", (7 * 24 * 60 * 60));

            modelo.addAttribute("usuariosAutenticados", usuariosAutenticados);
            return "login/pedir-usuario";
        }

        // Redirigir al paso correcto
        switch (estado) {
            case "/clave":
                return "redirect:/empapp/clave";
            case "/area-personal":
                modelo.addAttribute("usuario", ultimoUsuario);
                return "redirect:/empapp/area-personal";
            case null:
                modelo.addAttribute("usuariosAutenticados", usuariosAutenticados);
                return "login/pedir-usuario";
            default:
                modelo.addAttribute("usuariosAutenticados", usuariosAutenticados);
                return "login/pedir-usuario";
        }
    }

    @PostMapping("/procesar-usuario")
    public String procesarEmail(Model modelo,
                                HttpServletRequest request,
                                HttpServletResponse respuesta,
                                @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                BindingResult result) {
        if (!usuarioService.validarEmail(dto.getCorreo())) {
            modelo.addAttribute("ErrorCredenciales", "Usuario incorrecto");
            return "login/pedir-usuario";
        }

        // Guardar el correo en la sesión
        request.getSession().setAttribute("ultimoUsuario", dto.getCorreo());

        // Seguimos usando cookie para estado
        cookieService.crearCookie(respuesta, "estado", "/clave", (7 * 24 * 60 * 60));

        return "redirect:/empapp/clave";
    }

    @GetMapping("/clave")
    public String mostrarContrasena(Model modelo,
                                    HttpServletRequest request,
                                    @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                    @CookieValue(name = "estado", required = false) String estado) {

        if (estado == null || !estado.equals("/clave")) {
            return "redirect:/empapp/login";
        }

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        if (ultimoUsuario == null || !usuarioService.validarEmail(ultimoUsuario)) {
            return "redirect:/empapp/login";
        }

        modelo.addAttribute("correo", ultimoUsuario);
        return "login/pedir-clave";
    }

    @PostMapping("/procesar-clave")
    public String procesarContrasena(Model modelo,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     @CookieValue(name = "usuario", required = false) String valor,
                                     @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                     BindingResult result) {

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");

        if (ultimoUsuario == null || !usuarioService.validarEmail(ultimoUsuario)) {
            return "redirect:/empapp/login";
        }

        if (!usuarioService.validarCredenciales(new LoginUsuarioEmpleadoDTO(ultimoUsuario, dto.getClave()))) {
            modelo.addAttribute("correo", ultimoUsuario);
            modelo.addAttribute("error", "Contraseña incorrecta. Vuelva a intentarlo.");
            return "login/pedir-clave";
        }

        if (!cookieService.validar(valor)) {
            valor = "";
        }

        Map<String, Integer> usuarios = cookieService.deserializar(valor);
        if (usuarios == null) usuarios = new HashMap<>();

        String valorActualizado = cookieService.actualizar(usuarios, valor, ultimoUsuario);
        int contador = usuarios.getOrDefault(ultimoUsuario, 1);

        cookieService.crearCookie(response, "usuario", valorActualizado, (7 * 24 * 60 * 60));
        cookieService.crearCookie(response, "estado", "/area-personal", (7 * 24 * 60 * 60)); // corregido también aquí

        modelo.addAttribute("correo", ultimoUsuario);
        modelo.addAttribute("contador", contador);

        return "redirect:/empapp/area-personal";
    }

    @GetMapping("/area-personal")
    public String mostrarAreaPersonal(Model modelo,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      @CookieValue(name = "estado", required = false) String estado,
                                      @CookieValue(name = "usuario", required = false) String usuariosCookie) {

        if (estado == null || !estado.equals("/area-personal")) {
            return "redirect:/empapp/login";
        }

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        if (ultimoUsuario == null || !usuarioService.validarEmail(ultimoUsuario)) {
            return "redirect:/empapp/login";
        }

        Map<String, Integer> usuariosAutenticados = (cookieService.validar(usuariosCookie))
                ? cookieService.deserializar(usuariosCookie) : null;

        int contador = usuariosAutenticados.getOrDefault(ultimoUsuario, 1);

        modelo.addAttribute("correo", ultimoUsuario);
        modelo.addAttribute("contador", contador);
        modelo.addAttribute("dto", contador);
        request.getSession().setAttribute("usuarioLogeado", ultimoUsuario);
        return "area-personal";
    }

    @GetMapping("/desconectar")
    public String cerrarSesion(HttpServletResponse response,
                               HttpSession sesion) {
        sesion.removeAttribute("ultimoUsuario");
        sesion.removeAttribute("usuarioLogeado");
        return "redirect:/empapp/login";
    }

    @GetMapping("/devuelve-clave")
    @ResponseBody
    public String devuelveClave(@RequestParam(required = false) String correo) {
        return usuarioService.devuelveUsuarioEmpPorCorreo(correo).getClave();
    }
}
