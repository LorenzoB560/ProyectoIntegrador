package org.grupob.adminapp.controller;



import jakarta.persistence.EntityNotFoundException;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.adminapp.service.EmpleadoServiceImp;
import org.grupob.adminapp.service.EtiquetaServiceImp;
import org.grupob.adminapp.service.UsuarioEmpleadoServiceImp;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;


import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("empleados")
public class EmpleadoRestController {

    private final EmpleadoRepository empleadoRepository;
    private EmpleadoServiceImp empleadoService;
    private final EtiquetaServiceImp etiquetaService; // Inyectar EtiquetaService
    private final UsuarioEmpleadoServiceImp usuarioEmpleadoService;
    public EmpleadoRestController(EmpleadoServiceImp empleadoService, EmpleadoRepository empleadoRepository, EtiquetaServiceImp etiquetaService, UsuarioEmpleadoServiceImp usuarioEmpleadoService) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoService = empleadoService;
        this.etiquetaService = etiquetaService;
        this.usuarioEmpleadoService = usuarioEmpleadoService;
    }

    @GetMapping("/listado1")
    public List<EmpleadoDTO> listarEmpleados() {
        return empleadoService.devuelveTodosEmpleados();
    }

    @GetMapping("/listado")
    public Page<EmpleadoDTO> listarEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String comentario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contratadosAntesDe,
            @RequestParam(required = false) BigDecimal salarioMinimo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ename") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return empleadoService.buscarEmpleadosPaginados(
                nombre, departamento, comentario, contratadosAntesDe, salarioMinimo,
                page, size, sortBy, sortDir);
    }


    @GetMapping("detalle/{id}")
    public EmpleadoDTO devuelveEmpleado(@PathVariable String id){
        return empleadoService.devuelveEmpleado(id);
    }

    @GetMapping("nombre/{nombre}")
    public Empleado devuelveEmpleadoPorNombre(@PathVariable String nombre){
        return empleadoService.devuleveEmpleadoPorNombre(nombre);
    }

    @PostMapping("alta")
    public Empleado guardarEmpleado(@RequestBody Empleado empleado){
        return empleadoService.guardarEmpleado(empleado);
    }

    @DeleteMapping("borrar/{id}")
    public void borrarEmpleado(@PathVariable String id){
        empleadoService.eliminaEmpleadoPorId(id);
    }

    @PutMapping("modificar/{id}")
    public Empleado modificarEmpleado(@PathVariable String id, @RequestBody Empleado empleado){
        return empleadoService.modificarEmpleado(id, empleado);
    }

    @PutMapping("/{id}/jefe/{jefeId}")
    public EmpleadoDTO asignarJefe(@PathVariable String id, @PathVariable String jefeId) {
        return empleadoService.asignarJefe(id, jefeId);
    }

    @DeleteMapping("/{id}/jefe")
    public EmpleadoDTO quitarJefe(@PathVariable String id) {
        return empleadoService.quitarJefe(id);
    }

    @GetMapping("/{id}/subordinados")
    public List<EmpleadoDTO> listarSubordinados(@PathVariable String id) {
        return empleadoService.listarSubordinados(id);
    }
    // --- NUEVO ENDPOINT PARA BUSCAR POR ETIQUETA ---
    @GetMapping("/por-etiqueta/{etiquetaId}")
    public ResponseEntity<List<EmpleadoDTO>> buscarEmpleadosPorEtiqueta(@PathVariable String etiquetaId) {
        try {
            // Validar UUID
            UUID.fromString(etiquetaId);
            List<EmpleadoDTO> empleados = etiquetaService.buscarEmpleadosPorEtiqueta(etiquetaId);
            return ResponseEntity.ok(empleados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // ID de etiqueta inválido
        } catch (DepartamentoNoEncontradoException e) { // Si la etiqueta no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>()); // Devuelve lista vacía en Not Found
        } catch (Exception e) {
            System.err.println("Error al buscar empleados por etiqueta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/desbloquear")
    public ResponseEntity<?> quitarBloqueo(@PathVariable String id) {
        try {
            // 1. Buscar al Empleado y su UsuarioEmpleado asociado

            UUID empleadoUuid = UUID.fromString(id); // Convertir String a UUID
            Empleado empleado = empleadoRepository.findById(empleadoUuid)
                    .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

            UsuarioEmpleado usuario = empleado.getUsuario();
            if (usuario == null) {
                // Considera qué hacer si un empleado no tiene usuario (¿error?)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario asociado al empleado no encontrado.");
            }

            // 2. Comprobar si el usuario YA está desbloqueado
            // Asumimos que "desbloqueado" significa motivoBloqueo es null
            if (usuario.getMotivoBloqueo() == null ) {
                // Devolver un 400 Bad Request indicando que la operación no es aplicable
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El empleado ya se encuentra desbloqueado.");
            }

            // 3. Si está bloqueado, proceder a desbloquear
            usuarioEmpleadoService.desbloquearEmpleado(id); // Llama al servicio


            // 4. Devolver éxito
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            // Captura el error si el ID proporcionado no es un UUID válido
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido: " + id);
        } catch (EntityNotFoundException e) {
            // Captura si el empleado no se encuentra
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al intentar desbloquear el empleado: " + e.getMessage());
        }
    }

    // --- MÉTODO MODIFICADO ---
    @PostMapping("/{id}/bloquear")
    public ResponseEntity<?> asignarBloqueo(@PathVariable String id, @RequestParam Long motivoId) {
        try {
            // 1. Buscar al Empleado y su UsuarioEmpleado asociado
            UUID empleadoUuid = UUID.fromString(id); // Convertir String a UUID
            Empleado empleado = empleadoRepository.findById(empleadoUuid)
                    .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

            UsuarioEmpleado usuario = empleado.getUsuario();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario asociado al empleado no encontrado.");
            }

            // 2. Comprobar si el usuario YA está bloqueado o inactivo
            // Usamos !isActivo() como indicador principal.
            if (usuario.getMotivoBloqueo() != null ) {
                // Devolver un 400 Bad Request indicando que la operación no es aplicable
                // Puedes añadir el motivo actual si quieres:
                String mensaje = "El empleado ya se encuentra bloqueado";
                if (usuario.getMotivoBloqueo() != null) {
                    mensaje += " (Motivo: " + usuario.getMotivoBloqueo().getMotivo() + ")";
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
            }

            // 3. Si está activo, proceder a bloquear
            usuarioEmpleadoService.bloquearEmpleado(id, motivoId); // Llama al servicio


            // 4. Devolver éxito (el JS redirigirá)
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            // Captura el error si el ID proporcionado no es un UUID válido
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido: " + id);
        } catch (EntityNotFoundException e) {
            // Captura si el empleado o el motivo (dentro del service) no se encuentran
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            // Loggear error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al intentar bloquear el empleado: " + e.getMessage());
        }
    }

//    @PutMapping("/{id}/etiquetas/{etiquetaId}")
//    public EmpleadoDTO asignarEtiqueta(@PathVariable String id, @PathVariable String etiquetaId) {
//        return empleadoService.asignarEtiqueta(id, etiquetaId);
//    }
//
//    @DeleteMapping("/{id}/etiquetas/{etiquetaId}")
//    public EmpleadoDTO quitarEtiqueta(@PathVariable String id, @PathVariable String etiquetaId) {
//        return empleadoService.quitarEtiqueta(id, etiquetaId);
//    }
//
//    @GetMapping("/etiquetas/{etiquetaId}")
//    public List<EmpleadoDTO> buscarPorEtiqueta(@PathVariable String etiquetaId) {
//        return empleadoService.buscarPorEtiqueta(etiquetaId);
//    }

}
