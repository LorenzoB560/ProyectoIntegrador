package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.service.CookieService;
import org.grupob.empapp.service.EmpleadoServiceImp; // Necesario para validar que el jefe existe
import org.grupob.comun.exception.DepartamentoNoEncontradoException; // O la excepción que uses para 'no encontrado'
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.SessionAttribute; // Opcional: si obtienes el jefe de la sesión

import java.util.UUID;

@Controller
@RequestMapping("/etiquetado") // Ruta base para las vistas de etiquetado
public class EtiquetadoController {

    private final EmpleadoServiceImp empleadoService; // Inyectamos EmpleadoService para validar al jefe
    private final CookieService cookieService;
    public EtiquetadoController(EmpleadoServiceImp empleadoService, CookieService cookieService) {
        this.empleadoService = empleadoService;
        this.cookieService = cookieService;
    }

    /**
     * Muestra la vista de etiquetado masivo para un jefe específico.
     * @param jefeId El ID del jefe cuyos subordinados y etiquetas se mostrarán.
     * @param model El modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf.
     */
    @GetMapping("/masivo/{jefeId}")
    public String mostrarEtiquetadoMasivo(@PathVariable String jefeId,
                                          Model model,
                                          @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                          HttpServletRequest request) {
        // --- Validación Opcional ---
        // Es buena práctica validar que el jefe existe antes de mostrar la página.
        // También, en una aplicación real, verificarías si el usuario logueado
        // tiene permiso para etiquetar como este jefe.
        try {
            // Intenta convertir a UUID y busca al empleado para asegurar que existe
            UUID.fromString(jefeId);
            empleadoService.devuelveEmpleado(jefeId); // Lanza excepción si no existe

            model.addAttribute("jefeId", jefeId); // Pasa el ID del jefe a la vista
            // Asegúrate de que la plantilla exista en la ruta correcta
            LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

            if(dto==null){
                return "redirect:/empapp/login";
            }

            model.addAttribute("dto", dto);
            String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
            model.addAttribute("ultimaPagina", ultimaPagina);

            String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
            int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
            model.addAttribute("contador", contador);

            return "etiquetado/etiquetado-masivo";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "El ID del jefe proporcionado no es válido.");
            System.err.println("ID de jefe inválido en la URL: " + jefeId);
            return "error"; // O una vista de error genérica
        } catch (DepartamentoNoEncontradoException e) {
            model.addAttribute("error", "El jefe con ID " + jefeId + " no existe.");
            System.err.println(e.getMessage());
            return "error"; // O una vista de error genérica
        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado al cargar la página de etiquetado.");
            System.err.println("Error en EtiquetadoController: " + e.getMessage());
            e.printStackTrace();
            return "error"; // O una vista de error genérica
        }
        // --- Fin Validación Opcional ---

        // Si no haces validación aquí (confiando en que el servicio lo hará después),
        // simplemente pasarías el ID y devolverías la vista:
        // model.addAttribute("jefeId", jefeId);
        // return "etiquetado/etiquetado-masivo";
    }

    // Aquí podrías añadir más métodos @GetMapping para otras vistas relacionadas
    // con el etiquetado si las necesitas (ej. etiquetado simple, eliminar etiquetas).
    // @GetMapping("/simple/{jefeId}")
    // public String mostrarEtiquetadoSimple(...) { ... }
    // --- NUEVO MÉTODO ---
    @GetMapping("/limitado/{jefeId}")
    public String mostrarNuevoEtiquetado(@PathVariable String jefeId,
                                         Model model,
                                         @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                         HttpServletRequest request) {
        try {
            // Validación opcional del jefe (recomendado)
            UUID.fromString(jefeId);
            empleadoService.devuelveEmpleado(jefeId); // Lanza excepción si no existe

            model.addAttribute("jefeId", jefeId); // Pasar ID a la vista

            LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

            if(dto==null){
                return "redirect:/empapp/login";
            }

            model.addAttribute("dto", dto);
            String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
            model.addAttribute("ultimaPagina", ultimaPagina);

            String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
            int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
            model.addAttribute("contador", contador);

            return "etiquetado/limitado-etiquetado"; // Nombre de la nueva plantilla HTML

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "El ID del jefe proporcionado no es válido.");
            return "error"; // O una vista de error genérica
        } catch (DepartamentoNoEncontradoException e) { // Asumiendo que esta es tu excepción
            model.addAttribute("error", "El jefe con ID " + jefeId + " no existe.");
            return "error"; // O una vista de error genérica
        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado al cargar la página de etiquetado.");
            e.printStackTrace(); // Loguear el error completo
            return "error"; // O una vista de error genérica
        }
    }
    // --- NUEVO MÉTODO PARA LA VISTA DE ELIMINAR ---
    @GetMapping("/eliminar/{jefeId}")
    public String mostrarEliminarEtiquetas(@PathVariable String jefeId,
                                           Model model,
                                           @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                           HttpServletRequest request) {
        try {
            // Validación opcional del jefe (recomendado)
            UUID.fromString(jefeId);
            empleadoService.devuelveEmpleado(jefeId); // Lanza excepción si no existe

            model.addAttribute("jefeId", jefeId); // Pasar ID a la vista

            LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

            if(dto==null){
                return "redirect:/empapp/login";
            }

            model.addAttribute("dto", dto);

            String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
            model.addAttribute("ultimaPagina", ultimaPagina);
            String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
            int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
            model.addAttribute("contador", contador);

            return "etiquetado/eliminar-etiquetas"; // Nombre de la nueva plantilla HTML

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "El ID del jefe proporcionado no es válido.");
            return "error";
        } catch (DepartamentoNoEncontradoException e) { // O tu excepción de no encontrado
            model.addAttribute("error", "El jefe con ID " + jefeId + " no existe.");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado al cargar la página de eliminación.");
            e.printStackTrace();
            return "error";
        }
    }

}