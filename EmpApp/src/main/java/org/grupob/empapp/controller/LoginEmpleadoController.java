package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        Map<String, Integer> usuariosAutenticados = (CookieService.validar(usuariosCookie))
                ? CookieService.deserializar(usuariosCookie) : null;
        String estado = CookieService.obtenerValorCookie(request, "estado");
        String ultimoUsuario = CookieService.obtenerValorCookie(request, "ultimoUsuario");

        if (ultimoUsuario == null || (usuariosAutenticados != null && !usuariosAutenticados.containsKey(ultimoUsuario))) {
            CookieService.crearCookie(respuesta, "estado", "login", (7 * 24 * 60 * 60));
            CookieService.crearCookie(respuesta, "ultimoUsuario", "", (7 * 24 * 60 * 60));


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
                                HttpServletResponse respuesta,
                                @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                BindingResult result) {
        if (!usuarioService.validarEmail(dto.getCorreo())) {
            modelo.addAttribute("ErrorCredenciales", "Usuario incorrecto");
            return "login/pedir-usuario";
        }
        CookieService.crearCookie(respuesta, "ultimoUsuario", dto.getCorreo(), (7 * 24 * 60 * 60));
        CookieService.crearCookie(respuesta, "estado", "/clave", (7 * 24 * 60 * 60));
        return "redirect:/empapp/clave"; // Redirige a la vista de clave
    }

    @GetMapping("/clave")
    public String mostrarContrasena(Model modelo,
                                    HttpServletRequest request,
                                    @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                    @CookieValue(name = "estado", required = false) String estado,
                                    @CookieValue(name = "ultimoUsuario", required = false) String ultimoUsuario) {

        // Verificar si el estado es válido
        if (estado == null || !estado.equals("/clave")) {
            return "redirect:/empapp/login"; // Redirigir si no está en el flujo correcto
        }

       /* // Verificar que ultimoUsuario sea válido
        if (ultimoUsuario == null || !usuarioService.validarEmail(dto.getCorreo())) {
            return "redirect:/empapp/login"; // Redirigir si el usuario no es válido
        }*/


        modelo.addAttribute("correo", ultimoUsuario);
        return "login/pedir-clave";
    }

    @PostMapping("/procesar-clave")
    public String procesarContrasena(Model modelo,
                                     HttpServletResponse response,
                                     @CookieValue(name = "usuario", required = false) String valor,
                                     @CookieValue(name = "ultimoUsuario", required = false) String ultimoUsuario,
                                     @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                     BindingResult result) {

        // Verificar que ultimoUsuario sea válido
        if (ultimoUsuario == null || !usuarioService.validarEmail(ultimoUsuario)) {
            modelo.addAttribute("correo", ultimoUsuario);
            return "redirect:/empapp/login"; // Redirigir si el usuario no es válido
        }

        // Verificar si la clave es correcta
        if (!usuarioService.validarCredenciales(new LoginUsuarioEmpleadoDTO(ultimoUsuario, dto.getClave()))) {
            modelo.addAttribute("correo", ultimoUsuario);
            modelo.addAttribute("error", "Contraseña incorrecta. Vuelva a intentarlo.");
            return "login/pedir-clave";
        }

        // Verificar si la cookie "usuario" tiene datos válidos
        if (!CookieService.validar(valor)) {
            valor = ""; // Evita valores inválidos
        }

        // Deserializar usuarios
        Map<String, Integer> usuarios = CookieService.deserializar(valor);
        if (usuarios == null) {
            usuarios = new HashMap<>();
        }

        // Actualizar la cookie de usuarios autenticados
        String valorActualizado = CookieService.actualizar(usuarios, valor, ultimoUsuario);
        int contador = usuarios.getOrDefault(ultimoUsuario, 1);

        CookieService.crearCookie(response, "usuario", valorActualizado, (7 * 24 * 60 * 60));


        modelo.addAttribute("correo", ultimoUsuario);
        modelo.addAttribute("contador", contador);

        return "redirect:/empapp/area-personal";
    }

    @GetMapping("/area-personal")
    public String mostrarAreaPersonal(Model modelo,
                                      HttpServletResponse response,
                                      @CookieValue(name = "estado", required = false) String estado,
                                      @CookieValue(name = "ultimoUsuario", required = false) String ultimoUsuario,
                                      @CookieValue(name = "usuario", required = false) String usuariosCookie){
        // Verificar si el estado es válido
        if (estado == null || !estado.equals("/clave")) {
            return "redirect:/empapp/login"; // Redirigir si no está en el flujo correcto
        }

        // Verificar que ultimoUsuario sea válido
        if (ultimoUsuario == null || !usuarioService.validarEmail(ultimoUsuario)) {
            return "redirect:/empapp/login"; // Redirigir si el usuario no es válido
        }


        Map<String, Integer> usuariosAutenticados = (CookieService.validar(usuariosCookie))
                ? CookieService.deserializar(usuariosCookie) : null;

        // Obtener el contador del usuario actual
        int contador = usuariosAutenticados.getOrDefault(ultimoUsuario, 1);

        // Pasar los valores a la vista
        modelo.addAttribute("correo", ultimoUsuario);
        modelo.addAttribute("contador", contador);
        modelo.addAttribute("dto", contador);

        CookieService.crearCookie(response, "estado", "/area-perosnal", (7 * 24 * 60 * 60));

        return "area-personal";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpServletResponse response) {
        cookieService.cerrarSesion(response, "ultimoUsuario");
        return "redirect:/empapp/login";
    }

    @GetMapping("/devuelve-clave")
    @ResponseBody
    public String devuelveClave(@RequestParam(required = false) String usuario) {
        return null;
    }
}




