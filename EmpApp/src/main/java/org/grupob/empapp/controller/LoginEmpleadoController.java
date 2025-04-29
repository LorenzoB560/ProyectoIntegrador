package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.exception.ClaveIncorrectaException;
import org.grupob.comun.exception.CuentaBloqueadaException;
import org.grupob.comun.exception.UsuarioNoEncontradoException;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.ActualizarClaveDTO;
import org.grupob.empapp.dto.grupo_validaciones.GrupoClave;
import org.grupob.empapp.dto.grupo_validaciones.GrupoUsuario;
import org.grupob.empapp.service.CookieService;
import org.grupob.empapp.service.EmpleadoServiceImp;
import org.grupob.empapp.service.UsuarioEmpleadoServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("empapp")
public class LoginEmpleadoController {

    private final Logger logger = LoggerFactory.getLogger(LoginEmpleadoController.class);
    private final UsuarioEmpleadoServiceImp usuarioService;
    private final CookieService cookieService;
    private final EmpleadoServiceImp empleadoServiceImp;

    public LoginEmpleadoController(UsuarioEmpleadoServiceImp usuarioService,
                                   CookieService cookieService, EmpleadoServiceImp empleadoServiceImp) {
        this.usuarioService = usuarioService;
        this.cookieService = cookieService;
        this.empleadoServiceImp = empleadoServiceImp;
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

        // Si no hay usuario en sesión o no está en la cookie de autenticados, mostrar selección de usuario
        if (ultimoUsuario == null || (usuariosAutenticados != null && !usuariosAutenticados.containsKey(ultimoUsuario))) {
            cookieService.crearCookie(respuesta, "estado", "login", (7 * 24 * 60 * 60));

            modelo.addAttribute("usuariosAutenticados", usuariosAutenticados);
            return "login/pedir-usuario";
        }

        // Redirigir al paso correcto según el estado almacenado
        switch (estado) {
            case "/clave":
                return "redirect:/empapp/clave";
           /* case "/area-personal":
                modelo.addAttribute("usuario", ultimoUsuario);
                return "redirect:/empapp/area-personal";*/
            case null:
                modelo.addAttribute("usuariosAutenticados", usuariosAutenticados);
                return "login/pedir-usuario";
            default:
                modelo.addAttribute("usuariosAutenticados", usuariosAutenticados);
                return "login/pedir-usuario";
        }
    }

    @PostMapping("/procesar-usuario")
    public String procesarUsuario(Model modelo,
                                  HttpServletRequest request,
                                  HttpServletResponse respuesta,
                                  @Validated(GrupoUsuario.class) @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                  BindingResult result) {


        if (result.hasErrors()) {
            return "login/pedir-usuario";
        }
        String estado = cookieService.obtenerValorCookie(request, "estado");
        // Si el estado es área personal, ir directamente
        if ("/area-personal".equals(estado)) {
            return "redirect:/empapp/area-personal";
        }

        try {
            if (!usuarioService.validarEmail(dto.getUsuario())) {
                modelo.addAttribute("ErrorCredenciales", "Usuario incorrecto");
                return "login/pedir-usuario";
            }
        } catch (UsuarioNoEncontradoException e) {
            modelo.addAttribute("error", e.getMessage());
            return "login/pedir-usuario";
        } catch (CuentaBloqueadaException e) {
            modelo.addAttribute("errorBloqueo",
                    String.format("%s. Cuenta bloqueada hasta el %s",
                            e.getMessage(),
                            e.getFechaDesbloqueo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    ));
            return "login/pedir-usuario";
        }

        // Guardar el correo en la sesión
        request.getSession().setAttribute("ultimoUsuario", dto.getUsuario());

        // Seguimos usando cookie para estado
        cookieService.crearCookie(respuesta, "estado", "/clave", (7 * 24 * 60 * 60));

        return "redirect:/empapp/clave";
    }

    @GetMapping("/clave")
    public String mostrarClave(Model modelo,
                               HttpServletRequest request,
                               @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto) {

        String estado = cookieService.obtenerValorCookie(request, "estado");
        if (estado == null || !estado.equals("/clave")) {
            return "redirect:/empapp/login";
        }

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        if (ultimoUsuario == null || !usuarioService.validarEmail(ultimoUsuario)) {
            return "redirect:/empapp/login";
        }

        modelo.addAttribute("usuario", ultimoUsuario);
        return "login/pedir-clave";
    }

    @PostMapping("/procesar-clave")
    public String procesarContrasena(Model modelo,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                     @Validated(GrupoClave.class) @ModelAttribute("dto") LoginUsuarioEmpleadoDTO dto,
                                     BindingResult result) {
        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        //DEBERIA VALIDARSE LA CONTRASEÑA INTRODUCIDA? PARA MI EN PRINCIPIO NO
        //Comprobacion inecesario
        /* if (result.hasErrors()) {
            modelo.addAttribute("usuario", ultimoUsuario);
            return "login/pedir-clave";
        }*/

        // 1. Verificar nulidad del usuario antes de cualquier operación
        if (ultimoUsuario == null) {
            modelo.addAttribute("error", "Sesión expirada o modificación detectada. Vuelva a autenticarse.");
            return "redirect:/empapp/login";
        }

        try {

            // 2. Validación de credenciales con manejo de excepciones específicas
            if (!usuarioService.validarCredenciales(new LoginUsuarioEmpleadoDTO(ultimoUsuario, dto.getClave()))) {
                modelo.addAttribute("usuario", ultimoUsuario);
//                modelo.addAttribute("error", "Error interno de validación");
                return "login/pedir-clave";
            }

            // 3. Manejo de cookies (protección contra eliminación maliciosa)
            Map<String, Integer> usuarios = cookieService.deserializar(usuariosCookie);
            if (usuarios == null) usuarios = new HashMap<>();

            // 4. Actualización segura del contador
            String valorActualizado = cookieService.actualizar(usuarios, usuariosCookie, ultimoUsuario);
            cookieService.crearCookie(response, "usuario", valorActualizado, 604800); // 7 días en segundos

            cookieService.crearCookie(response, "usuario", valorActualizado, (7 * 24 * 60 * 60));
            cookieService.crearCookie(response, "estado", "/area-personal", (7 * 24 * 60 * 60)); // corregido también aquí

            int contador = usuarios.getOrDefault(ultimoUsuario, 1);

            modelo.addAttribute("usuario", ultimoUsuario);
            modelo.addAttribute("contador", contador);

            return "redirect:/empapp/area-personal";

        } catch (UsuarioNoEncontradoException e) {
            // Registro de intento sospechoso
            request.getSession().invalidate();
            modelo.addAttribute("error", "Credenciales inválidas. Sesión reiniciada");
            return "redirect:/empapp/login";

        } catch (CuentaBloqueadaException e) {
            modelo.addAttribute("usuario", ultimoUsuario);
            modelo.addAttribute("errorBloqueo",
                    String.format("%s. Usuario %s bloqueado hasta el %s",
                            e.getMessage(),
                            ultimoUsuario,
                            e.getFechaDesbloqueo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    ));
            return "login/pedir-usuario";

        } catch (ClaveIncorrectaException e) {
            modelo.addAttribute("usuario", ultimoUsuario);
            modelo.addAttribute("error", e.getMessage() + ", intentos restantes: " + e.getIntentosRestantes());
            return "login/pedir-clave";
        }
    }


    @GetMapping("/area-personal")
    public String mostrarAreaPersonal(Model modelo,
                                      HttpServletRequest request,
                                      @CookieValue(name = "usuario", required = false) String usuariosCookie) {
        String estado = cookieService.obtenerValorCookie(request, "estado");
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");

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
        request.getSession().setAttribute("usuarioLogeado", usuarioService.devuelveUsuarioEmpPorUsuario(ultimoUsuario));

        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");
        modelo.addAttribute("dto", dto);

//        modelo.addAttribute("usuario", ultimoUsuario);
        modelo.addAttribute("contador", contador);
        modelo.addAttribute("ultimaPagina", ultimaPagina);

        EmpleadoDTO emp = empleadoServiceImp.devuelveEmpleado(String.valueOf(dto.getId()));
        System.err.println(emp);

        /*if(emp==null){
            return "login/area-personal";
        }*/
        logger.info("Autenticacion exitosa del usuario: {}", ultimoUsuario);
        return "login/area-personal";
//        return "redirect:/datos-personales";
    }

    @GetMapping("/seleccionar-otra-cuenta")
    public String seleccionarOtraCuenta(HttpServletResponse response) {
        cookieService.eliminarCookie(response, "estado"); // o el nombre de tu cookie
        return "redirect:/empapp/login";
    }

    @GetMapping("/desconectar")
    public String cerrarSesion(HttpServletResponse response, HttpServletRequest request,
                               HttpSession sesion) {
        sesion.removeAttribute("ultimoUsuario");
        sesion.removeAttribute("usuarioLogeado");

        String ultimaPagina = request.getHeader("Referer");

        cookieService.crearCookie(response, "ultimaPagina", ultimaPagina, (7 * 24 * 60 * 60));
        return "redirect:/empapp/login";
    }

    @GetMapping("/devuelve-clave")
    @ResponseBody
    public String devuelveClave(@RequestParam(required = false) String usuario) {
        return usuarioService.devuelveUsuarioEmpPorUsuario(usuario).getClave();
    }

    @GetMapping("/redirigir-hacia-registro-usuario")
    public String redirigirLogin() {
        return "redirect:/registro-usuario";
    }

    @GetMapping("/actualizacion-clave")
    public String redirigirActualizarClave(Model modelo, HttpServletRequest request) {
        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        ActualizarClaveDTO dto = new ActualizarClaveDTO(ultimoUsuario);
        modelo.addAttribute("dto", dto);
        return "login/actualizar-clave";
    }


    @PostMapping("/procesar-actualizacion-clave")
    public String actualizarClave(Model modelo,
                                  HttpServletRequest request,
                                  @Valid @ModelAttribute("dto") ActualizarClaveDTO dto,
                                  BindingResult result) {

        if (result.hasErrors()) {
            return "login/actualizar-clave";
        }

        if (!dto.getNuevaClave().equals(dto.getConfirmacionClave())) {
            modelo.addAttribute("error", "Las contraseñas no coinciden");
            return "login/actualizar-clave";
        }

        try {
            usuarioService.actualizarClave(dto.getUsuario(), dto.getNuevaClave());
            return "redirect:/empapp/clave";
        } catch (UsuarioNoEncontradoException e) {
            request.getSession().invalidate();
            modelo.addAttribute("error", "Detectado alteracion maliciosa. Sesión reiniciada");
            return "login/pedir-usuario";
        }
    }

}
