package org.grupob.empapp.controller;

import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.empapp.service.EtiquetaService; // *** CAMBIO: Importar la interfaz del servicio ***
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.empapp.service.EtiquetaServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/etiquetas")
public class EtiquetaRestController {

    private final EtiquetaServiceImp etiquetaService;

    public EtiquetaRestController(EtiquetaServiceImp etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    // --- Endpoints para listar etiquetas (sin cambios) ---
    @GetMapping("/jefe/{jefeId}")
    public ResponseEntity<List<EtiquetaDTO>> listarEtiquetasPorJefe(@PathVariable String jefeId) {
        // ... (código existente) ...
        try {
            UUID.fromString(jefeId);
            List<EtiquetaDTO> etiquetas = etiquetaService.listarEtiquetasPorJefe(jefeId);
            return ResponseEntity.ok(etiquetas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error al listar etiquetas por jefe: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<List<EtiquetaDTO>> listarTodasEtiquetasGlobales() {
        // ... (código existente) ...
        try {
            List<EtiquetaDTO> etiquetas = etiquetaService.listarTodasEtiquetasGlobales();
            return ResponseEntity.ok(etiquetas);
        } catch (Exception e) {
            System.err.println("Error al listar todas las etiquetas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- Endpoint para ASIGNAR una etiqueta a un empleado ---
    // Usamos PUT porque estamos actualizando el estado del empleado (sus etiquetas)
    // La ruta podría ser /api/empleados/{empleadoId}/etiquetas/{etiquetaId} y estar en EmpleadoRestController,
    // o /api/etiquetas/asignar/empleado/{empleadoId}/etiqueta/{etiquetaId} para mantenerlo aquí.
    // Vamos a usar la segunda opción para mantener la lógica de asignación aquí.
    @PutMapping("/asignar/empleado/{empleadoId}/etiqueta/{etiquetaId}")
    public ResponseEntity<EmpleadoDTO> asignarEtiqueta(
            @PathVariable String empleadoId,
            @PathVariable String etiquetaId,
            @RequestParam String jefeId) { // El jefe que realiza la acción, para validación
        // Podría venir del contexto de seguridad en un futuro
        try {
            // Validar IDs
            UUID.fromString(empleadoId);
            UUID.fromString(etiquetaId);
            UUID.fromString(jefeId);

            EmpleadoDTO empleadoActualizado = etiquetaService.asignarEtiquetaExistente(empleadoId, etiquetaId, jefeId);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (IllegalArgumentException | DepartamentoNoEncontradoException e) {
            System.err.println("Error al asignar etiqueta: " + e.getMessage());
            // Devolver 400 Bad Request o 404 Not Found según la excepción
            if (e instanceof DepartamentoNoEncontradoException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            System.err.println("Error inesperado al asignar etiqueta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- Endpoint para QUITAR una etiqueta de un empleado ---
    @DeleteMapping("/eliminar/empleado/{empleadoId}/etiqueta/{etiquetaId}")
    public ResponseEntity<EmpleadoDTO> quitarEtiqueta(
            @PathVariable String empleadoId,
            @PathVariable String etiquetaId,
            @RequestParam String jefeId) { // Jefe para validación
        try {
            // Validar IDs
            UUID.fromString(empleadoId);
            UUID.fromString(etiquetaId);
            UUID.fromString(jefeId);

            EmpleadoDTO empleadoActualizado = etiquetaService.eliminarEtiquetaDeEmpleado(empleadoId, etiquetaId, jefeId);
            return ResponseEntity.ok(empleadoActualizado); // Devuelve el empleado actualizado (con la etiqueta quitada)
        } catch (IllegalArgumentException | DepartamentoNoEncontradoException e) {
            System.err.println("Error al quitar etiqueta: " + e.getMessage());
            if (e instanceof DepartamentoNoEncontradoException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            System.err.println("Error inesperado al quitar etiqueta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // El endpoint asignarEtiquetaSimple (que crea la etiqueta si no existe) ya no se usa directamente aquí,
    // pero se mantiene en el servicio por si se necesita.

    // El endpoint de asignación masiva se elimina de este controlador.

}
