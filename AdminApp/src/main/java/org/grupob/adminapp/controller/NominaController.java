package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.FiltroNominaDTO;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.adminapp.service.NominaServiceImp;
import org.grupob.comun.entity.Nomina;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/nomina")
public class NominaController {

    private final NominaServiceImp nominaServiceImp;

    public NominaController(NominaServiceImp nominaServiceImp, AltaNominaServiceImp altaNominaServiceImp) {
        this.nominaServiceImp = nominaServiceImp;
    }

    @ModelAttribute
    public void adicionDatosModel(Model modelo) {
        modelo.addAttribute("meses", nominaServiceImp.devolverMeses());
        modelo.addAttribute("anios", nominaServiceImp.devolverAnios());
        modelo.addAttribute("listaNominas", nominaServiceImp.devolverNominas());
        modelo.addAttribute("listaConceptos", nominaServiceImp.devolverConceptos());
    }

    //TODO AÑADIR TABLA MAESTRA MESES, PARA MOSTRARLO EN LA NÓMINA
    @GetMapping("/listado")
    public String listarNominas() {
        return "listados/listado-vista-nomina";
    }

    @GetMapping("/detalle/{id}")
    public String vistaDetalleNomina(@PathVariable UUID id, Model model) {

        //Obtengo el DTO de la nómina según el ID en el servicio, y se lo paso a la vista
        model.addAttribute("nominaDTO", nominaServiceImp.devolverNominaPorId(id));

        return "listados/detalle-vista-nomina";
    }
    @GetMapping("/busqueda-parametrizada")
    public String listarNominasConFiltros(
            @ModelAttribute FiltroNominaDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Nomina> paginaNominas = nominaServiceImp.obtenerNominasFiltradas(filtro, page);

        model.addAttribute("listaNominas", paginaNominas.getContent());
        System.err.println("Nóminas encontradas: " + paginaNominas.getContent().size());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtro", filtro);

        return "listados/listado-vista-nomina";
    }

}
