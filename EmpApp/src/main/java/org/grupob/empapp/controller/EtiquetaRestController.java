package org.grupob.empapp.controller;

import org.grupob.comun.entity.Etiqueta;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.empapp.dto.NuevoEtiquetadoRequest;
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

    @GetMapping("/jefe/{jefeId}")
    public ResponseEntity<List<EtiquetaDTO>> listarEtiquetasPorJefe(@PathVariable String jefeId) {
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
        try {
            List<EtiquetaDTO> etiquetas = etiquetaService.listarTodasEtiquetasGlobales();
            return ResponseEntity.ok(etiquetas);
        } catch (Exception e) {
            System.err.println("Error al listar todas las etiquetas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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

    @GetMapping("/jefe/{jefeId}/sugerencias")
    public ResponseEntity<List<EtiquetaDTO>> obtenerSugerenciasEtiquetas(
            @PathVariable String jefeId,
            @RequestParam(defaultValue = "") String prefijo) {
        try {
            UUID jefeUuid = UUID.fromString(jefeId);
            List<EtiquetaDTO> sugerencias = etiquetaService.buscarEtiquetasPorPrefijo(jefeUuid, prefijo);
            return ResponseEntity.ok(sugerencias);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // ID inválido
        } catch (Exception e) {
            System.err.println("Error buscando sugerencias de etiquetas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // --- Endpoint crearOAsignar (con logs) ---
    @PostMapping("/crearOAsignar")
    public ResponseEntity<?> crearOAsignarEtiqueta(
            @RequestParam String jefeId,
            @RequestBody NuevoEtiquetadoRequest request) {

        System.out.println(">>> [crearOAsignar] Recibida petición:");
        System.out.println("    Jefe ID: " + jefeId);
        System.out.println("    Empleado IDs: " + request.getEmpleadoIds());
        System.out.println("    Nombre Etiqueta: '" + request.getNombreEtiqueta() + "'");


        if (request.getEmpleadoIds() == null || request.getEmpleadoIds().isEmpty()) {
            System.err.println(">>> [crearOAsignar] ERROR: No se seleccionaron empleados.");
            return ResponseEntity.badRequest().body("Debe seleccionar al menos un empleado.");
        }
        if (request.getNombreEtiqueta() == null || request.getNombreEtiqueta().trim().isEmpty()) {
            System.err.println(">>> [crearOAsignar] ERROR: Nombre de etiqueta vacío.");
            return ResponseEntity.badRequest().body("El nombre de la etiqueta no puede estar vacío.");
        }

        try {
            UUID jefeUuid = UUID.fromString(jefeId);

            // 1. Buscar o crear la etiqueta
            System.out.println(">>> [crearOAsignar] Llamando a buscarOCrearEtiqueta...");
            Etiqueta etiqueta = etiquetaService.buscarOCrearEtiqueta(request.getNombreEtiqueta(), jefeUuid);
            if (etiqueta == null || etiqueta.getId() == null) {
                System.err.println(">>> [crearOAsignar] ERROR: buscarOCrearEtiqueta devolvió null o sin ID.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No se pudo obtener o crear la etiqueta.");
            }
            System.out.println(">>> [crearOAsignar] Etiqueta obtenida/creada con ID: " + etiqueta.getId());


            // 2. Asignar la etiqueta a los empleados seleccionados
            int errores = 0;
            System.out.println(">>> [crearOAsignar] Iniciando bucle de asignación para " + request.getEmpleadoIds().size() + " empleado(s)...");
            for (String empleadoId : request.getEmpleadoIds()) {
                System.out.println(">>> [crearOAsignar] Intentando asignar a empleado: " + empleadoId);
                try {
                    etiquetaService.asignarEtiquetaExistente(empleadoId, etiqueta.getId().toString(), jefeId);
                    System.out.println(">>> [crearOAsignar] Asignación exitosa para empleado: " + empleadoId);
                } catch (Exception e) {
                    errores++;
                    System.err.println(">>> [crearOAsignar] ERROR asignando a empleado " + empleadoId + ": " + e.getMessage());
                    // Considera loguear el stack trace completo si necesitas más detalle: e.printStackTrace();
                }
            }
            System.out.println(">>> [crearOAsignar] Bucle de asignación finalizado. Errores: " + errores);

            // 3. Devolver resultado
            if (errores == 0) {
                String successMsg = "Etiqueta '" + etiqueta.getNombre() + "' asignada correctamente a " + request.getEmpleadoIds().size() + " empleado(s).";
                System.out.println(">>> [crearOAsignar] " + successMsg);
                return ResponseEntity.ok().body(successMsg);
            } else {
                String errorMsg = "Etiqueta asignada con " + errores + " errores de " + request.getEmpleadoIds().size() + " intentos.";
                System.err.println(">>> [crearOAsignar] " + errorMsg);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorMsg);
            }

        } catch (IllegalArgumentException e) {
            System.err.println(">>> [crearOAsignar] ERROR: ID de jefe inválido: " + jefeId + " -> " + e.getMessage());
            return ResponseEntity.badRequest().body("ID de jefe inválido: " + e.getMessage());
        } catch (DepartamentoNoEncontradoException e) { // Captura si buscarOCrear lanza error de jefe no encontrado
            System.err.println(">>> [crearOAsignar] ERROR: Jefe no encontrado al buscar/crear etiqueta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println(">>> [crearOAsignar] ERROR inesperado procesando nuevo etiquetado:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

}
