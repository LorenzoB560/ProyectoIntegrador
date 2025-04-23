package org.grupob.empapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("departamento")
public class DepartamentoController {
    @GetMapping("lista1")
    public String listadovista(){
        return "departamentos/listado-vista";
    }
    @GetMapping("detalle/{id}")
    public String listadoDepartamentoVista(@PathVariable String id, Model modelo){

        return "departamentos/detalle-vista";
    }


}
