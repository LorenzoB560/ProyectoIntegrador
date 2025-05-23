package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.EmpleadoSimpleDTO;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;
import org.grupob.empapp.service.ColaboracionService; // Cambiado a interfaz
import org.grupob.empapp.service.CookieService;
import org.grupob.empapp.service.EmpleadoService; // Cambiado a interfaz
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/empapp/colaboraciones")
@RequiredArgsConstructor
public class ColaboracionController {

    private final ColaboracionService colaboracionService;
    private final CookieService cookieService;

    private UUID getEmpleadoIdActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            LoginUsuarioEmpleadoDTO usuarioLogeado = (LoginUsuarioEmpleadoDTO) session.getAttribute("usuarioLogeado");
            if (usuarioLogeado != null) {
                return usuarioLogeado.getId();
            }
        }
        return null;
    }

    @GetMapping("/solicitar")
    public String mostrarFormularioSolicitud(Model modelo,
                                             @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                             HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return "redirect:/empapp/login";
        }

        try {
            List<EmpleadoSimpleDTO> empleadosParaSolicitar = colaboracionService.getOtrosEmpleados(empleadoIdActual)
                    .stream()
                    .map(e -> new EmpleadoSimpleDTO(e.getId(), e.getNombre() + " " + e.getApellido()))
                    .collect(Collectors.toList());
            modelo.addAttribute("empleados", empleadosParaSolicitar);
        } catch (Exception e) {
            modelo.addAttribute("errorMessage", "Error al cargar empleados: " + e.getMessage());
            modelo.addAttribute("empleados", List.of()); // Lista vacía en caso de error
        }
        // No se necesita "idReceptor" si el envío lo maneja el RestController
        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        modelo.addAttribute("dto", dto);
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        modelo.addAttribute("ultimaPagina", ultimaPagina);

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
        modelo.addAttribute("contador", contador);

        return "colaboraciones/enviar-solicitud";
    }

    @GetMapping("/listado")
    public String mostrarListados(Model modelo,
                                  @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                  HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return "redirect:/login";
        }

        try {
            List<SolicitudColaboracionDTO> recibidas = colaboracionService.getSolicitudesRecibidas(empleadoIdActual);
            List<SolicitudColaboracionDTO> enviadas = colaboracionService.getSolicitudesEnviadas(empleadoIdActual);

            modelo.addAttribute("solicitudesRecibidas", recibidas);
            modelo.addAttribute("solicitudesEnviadas", enviadas);
        } catch (Exception e) {
            modelo.addAttribute("errorMessage", "Error al cargar listados: " + e.getMessage());
            modelo.addAttribute("solicitudesRecibidas", List.of());
            modelo.addAttribute("solicitudesEnviadas", List.of());
        }

        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        modelo.addAttribute("dto", dto);
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        modelo.addAttribute("ultimaPagina", ultimaPagina);

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
        modelo.addAttribute("contador", contador);
        return "colaboraciones/listado-solicitudes";
    }
    @GetMapping("/historial")
    public String mostrarPaginaHistorialColaboraciones(Model modelo,
                                                       @CookieValue(name = "usuario", required = false) String usuariosCookie,
                                                       HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return "redirect:/empapp/login"; // Redirigir si no hay usuario en sesión
        }

        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        modelo.addAttribute("dto", dto);
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        modelo.addAttribute("ultimaPagina", ultimaPagina);

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
        modelo.addAttribute("contador", contador);
        return "colaboraciones/historial-colaboraciones";
    }
}