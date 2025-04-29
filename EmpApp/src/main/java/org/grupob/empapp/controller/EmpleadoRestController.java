package org.grupob.empapp.controller;



import org.grupob.comun.entity.Empleado;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.service.EmpleadoServiceImp;
import org.grupob.empapp.service.EtiquetaService;
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
    private final EtiquetaService etiquetaService; // Inyectar EtiquetaService

    public EmpleadoRestController(EmpleadoServiceImp empleadoService, EmpleadoRepository empleadoRepository, EtiquetaService etiquetaService) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoService = empleadoService;
        this.etiquetaService = etiquetaService;
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
