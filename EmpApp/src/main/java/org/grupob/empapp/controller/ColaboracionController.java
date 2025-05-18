package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.EmpleadoSimpleDTO;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;
import org.grupob.empapp.service.ColaboracionService; // Cambiado a interfaz
import org.grupob.empapp.service.EmpleadoService; // Cambiado a interfaz
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/empapp/colaboraciones")
public class ColaboracionController {

    private final ColaboracionService colaboracionService;
    // private final EmpleadoService empleadoService; // No es necesario aquí si solo cargas desde ColaboracionService

    @Autowired
    public ColaboracionController(ColaboracionService colaboracionService) {
        this.colaboracionService = colaboracionService;
    }

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
    public String mostrarFormularioSolicitud(Model model, HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return "redirect:/empapp/login";
        }

        try {
            List<EmpleadoSimpleDTO> empleadosParaSolicitar = colaboracionService.getOtrosEmpleados(empleadoIdActual)
                    .stream()
                    .map(e -> new EmpleadoSimpleDTO(e.getId(), e.getNombre() + " " + e.getApellido()))
                    .collect(Collectors.toList());
            model.addAttribute("empleados", empleadosParaSolicitar);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar empleados: " + e.getMessage());
            model.addAttribute("empleados", List.of()); // Lista vacía en caso de error
        }
        // No se necesita "idReceptor" si el envío lo maneja el RestController
        return "colaboraciones/enviar-solicitud";
    }

    @GetMapping("/listado")
    public String mostrarListados(Model model, HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return "redirect:/login";
        }

        try {
            List<SolicitudColaboracionDTO> recibidas = colaboracionService.getSolicitudesRecibidas(empleadoIdActual);
            List<SolicitudColaboracionDTO> enviadas = colaboracionService.getSolicitudesEnviadas(empleadoIdActual);

            model.addAttribute("solicitudesRecibidas", recibidas);
            model.addAttribute("solicitudesEnviadas", enviadas);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar listados: " + e.getMessage());
            model.addAttribute("solicitudesRecibidas", List.of());
            model.addAttribute("solicitudesEnviadas", List.of());
        }
        return "colaboraciones/listado-solicitudes";
    }
}