package org.grupob.empapp.controller;


import lombok.RequiredArgsConstructor;
import org.grupob.comun.dto.DepartamentoDTO;
import org.grupob.comun.entity.Departamento;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.empapp.service.DepartamentoServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departamentos")
@RequiredArgsConstructor
public class DepartamentoRestController {

    private final DepartamentoServiceImp departamentoService;

    @GetMapping("listado")
    public List<DepartamentoDTO>devuelveTodosDepartamentos(){
        return departamentoService.devuelveTodosDepartamentos();
    }

    @GetMapping("detalle/{id}")
    public DepartamentoDTO devuelveDepartamento(@PathVariable String id){
        return departamentoService.devuelveDepartamento(id);
    }

    @GetMapping("nombre/{nombre}")
    public Departamento devuelveDepartamentoPorNombre(@PathVariable String nombre){
        return departamentoService.devuleveDepartartamentoPorNombre(nombre);
    }

    @PostMapping("alta")
    public Departamento guardarDepartamento(@RequestBody Departamento departamento){
        return departamentoService.guardarDepartamento(departamento);
    }

    @DeleteMapping("borrar/{id}")
    public void borrarDepartamento(@PathVariable String id){
        departamentoService.eliminaDepartamentoPorId(id);
    }

    @PutMapping("modificar/{id}")
    public Departamento modificarDepartamento(@PathVariable String id, @RequestBody Departamento departamento){
        return departamentoService.modificarDepartamento(id, departamento);
    }

}
