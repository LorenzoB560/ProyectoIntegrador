package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.EmpleadoSimpleDTO;
import org.grupob.empapp.dto.HistorialColaboracionItemDTO;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;
import org.grupob.empapp.service.ColaboracionServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empapp/colaboraciones") // Ruta base distinta para la API
public class ColaboracionRestController {

    private final ColaboracionServiceImp colaboracionService;

    @Autowired
    public ColaboracionRestController(ColaboracionServiceImp colaboracionService) {
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

    @GetMapping("/empleados-para-solicitar")
    public ResponseEntity<?> getEmpleadosParaSolicitar(HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }

        try {
            List<EmpleadoSimpleDTO> empleados = colaboracionService.getOtrosEmpleados(empleadoIdActual)
                    .stream()
                    .map(e -> new EmpleadoSimpleDTO(e.getId(), e.getNombre() + " " + e.getApellido()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener empleados: " + e.getMessage()));
        }
    }

    // Endpoint para enviar una solicitud de colaboración
    @PostMapping("/solicitar")
    public ResponseEntity<?> enviarSolicitud(@RequestParam UUID idReceptor, HttpServletRequest request) {
        UUID idSolicitante = getEmpleadoIdActual(request);
        if (idSolicitante == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }
        if (idSolicitante.equals(idReceptor)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No puedes enviarte una solicitud de colaboración a ti mismo."));
        }

        try {
            colaboracionService.enviarSolicitudColaboracion(idSolicitante, idReceptor);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud de colaboración enviada correctamente."));
        } catch (Exception e) {
            // Considera códigos de estado más específicos si es posible
            // Por ejemplo, si la solicitud ya existe, podría ser un 409 Conflict.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint para obtener solicitudes recibidas (útil para actualizaciones AJAX)
    @GetMapping("/solicitudes/recibidas")
    public ResponseEntity<?> getSolicitudesRecibidas(HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }
        try {
            List<SolicitudColaboracionDTO> recibidas = colaboracionService.getSolicitudesRecibidas(empleadoIdActual);
            return ResponseEntity.ok(recibidas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener solicitudes recibidas: " + e.getMessage()));
        }
    }

    // Endpoint para obtener solicitudes enviadas (útil para actualizaciones AJAX)
    @GetMapping("/solicitudes/enviadas")
    public ResponseEntity<?> getSolicitudesEnviadas(HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }
        try {
            List<SolicitudColaboracionDTO> enviadas = colaboracionService.getSolicitudesEnviadas(empleadoIdActual);
            return ResponseEntity.ok(enviadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener solicitudes enviadas: " + e.getMessage()));
        }
    }

    @PostMapping("/solicitudes/{idSolicitud}/aceptar")
    public ResponseEntity<?> aceptarSolicitud(@PathVariable UUID idSolicitud, HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }

        try {
            colaboracionService.aceptarSolicitud(idSolicitud, empleadoIdActual);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud aceptada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/solicitudes/{idSolicitud}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable UUID idSolicitud, HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }

        try {
            colaboracionService.rechazarSolicitud(idSolicitud, empleadoIdActual);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud rechazada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<?> getHistorialColaboraciones(HttpServletRequest request) {
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }

        try {
            List<HistorialColaboracionItemDTO> historial = colaboracionService.getHistorialCompletoColaboraciones(empleadoIdActual);
            if (historial.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList()); // Devolver lista vacía si no hay historial
            }
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al cargar el historial de colaboraciones: " + e.getMessage()));
        }
    }
    @PostMapping("/{idColaboracion}/finalizar-periodo")
    public ResponseEntity<?> finalizarPeriodo(
            @PathVariable UUID idColaboracion,
            HttpServletRequest request) {
        // ... (sin cambios, ya que la lógica de quién finaliza ya no es restrictiva para reactivar)
        UUID empleadoIdActual = getEmpleadoIdActual(request);
        if (empleadoIdActual == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado."));
        }
        try {
            colaboracionService.finalizarPeriodoColaboracion(idColaboracion, empleadoIdActual);
            return ResponseEntity.ok(Map.of("mensaje", "Periodo de colaboración finalizado correctamente."));
        } catch (NoSuchElementException e) {
//            logger.warn("API: Intento de finalizar periodo de colaboración no encontrada ID {}: {}", idColaboracion, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
//            logger.warn("API: Intento no autorizado de finalizar periodo de colaboración ID {} por empleado ID {}: {}", idColaboracion, empleadoIdActual, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
//            logger.warn("API: Estado ilegal al intentar finalizar periodo de colaboración ID {}: {}", idColaboracion, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
//            logger.error("API: Error al finalizar periodo de colaboración ID {}: {}", idColaboracion, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al finalizar el periodo: " + e.getMessage()));
        }
    }



}