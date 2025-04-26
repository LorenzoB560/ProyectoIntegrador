package org.grupob.empapp.controller;

import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.empapp.service.EtiquetaService; // *** CAMBIO: Importar la interfaz del servicio ***
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/etiquetas")
public class EtiquetaRestController {

    private final EtiquetaService etiquetaService; // *** CAMBIO: Inyectar EtiquetaService ***

    // *** CAMBIO: Actualizar constructor ***
    public EtiquetaRestController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @GetMapping("/jefe/{jefeId}")
    public ResponseEntity<List<EtiquetaDTO>> listarEtiquetasPorJefe(@PathVariable String jefeId) {
        try {
            UUID.fromString(jefeId);
            List<EtiquetaDTO> etiquetas = etiquetaService.listarEtiquetasPorJefe(jefeId); // *** CAMBIO: Llamar a etiquetaService ***
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
            List<EtiquetaDTO> etiquetas = etiquetaService.listarTodasEtiquetasGlobales(); // *** CAMBIO: Llamar a etiquetaService ***
            return ResponseEntity.ok(etiquetas);
        } catch (Exception e) {
            System.err.println("Error al listar todas las etiquetas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/asignar-masivo")
    public ResponseEntity<Void> asignarEtiquetasMasivo(@RequestBody Map<String, List<String>> payload) {
        try {
            List<String> empleadoIds = payload.get("empleadoIds");
            List<String> etiquetaIds = payload.get("etiquetaIds");
            List<String> jefeIdList = payload.get("jefeId");

            if (jefeIdList == null || jefeIdList.isEmpty() || empleadoIds == null || etiquetaIds == null || empleadoIds.isEmpty() || etiquetaIds.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            String jefeId = jefeIdList.get(0);


            etiquetaService.asignarEtiquetasMasivo(jefeId, empleadoIds, etiquetaIds); // *** CAMBIO: Llamar a etiquetaService ***
            return ResponseEntity.ok().build();

        } catch (NullPointerException | IndexOutOfBoundsException | IllegalArgumentException e) {
            System.err.println("Error en la petición de etiquetado masivo: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (DepartamentoNoEncontradoException e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("Error inesperado en etiquetado masivo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/empleado/{empleadoId}/asignar/{jefeId}")
    public ResponseEntity<EmpleadoDTO> asignarEtiquetaSimple(
            @PathVariable String empleadoId,
            @PathVariable String jefeId,
            @RequestParam String nombreEtiqueta) {
        try {
            if (nombreEtiqueta == null || nombreEtiqueta.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            // *** CAMBIO: Llamar a etiquetaService ***
            EmpleadoDTO empleadoActualizado = etiquetaService.asignarEtiquetaSimple(empleadoId, nombreEtiqueta, jefeId);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (IllegalArgumentException | DepartamentoNoEncontradoException e) {
            System.err.println("Error al asignar etiqueta simple: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            System.err.println("Error inesperado al asignar etiqueta simple: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/empleado/{empleadoId}/etiqueta/{etiquetaId}/eliminar/{jefeId}")
    public ResponseEntity<EmpleadoDTO> eliminarEtiquetaDeEmpleado(
            @PathVariable String empleadoId,
            @PathVariable String etiquetaId,
            @PathVariable String jefeId) {
        try {
            // *** CAMBIO: Llamar a etiquetaService ***
            EmpleadoDTO empleadoActualizado = etiquetaService.eliminarEtiquetaDeEmpleado(empleadoId, etiquetaId, jefeId);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (IllegalArgumentException | DepartamentoNoEncontradoException e) {
            System.err.println("Error al eliminar etiqueta de empleado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            System.err.println("Error inesperado al eliminar etiqueta de empleado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
