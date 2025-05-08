package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.FiltroNominaDTO;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.adminapp.service.NominaServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
            @RequestParam(required = false) String filtroNombre,
            @RequestParam(required = false) Integer filtroMes,
            @RequestParam(required = false) Integer filtroAnio,
            @RequestParam(required = false) BigDecimal totalLiquidoMinimo,
            @RequestParam(required = false) BigDecimal totalLiquidoMaximo,
            @RequestParam(required = false) List<String> conceptosSeleccionados,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // Crear el filtro directamente con los parámetros recibidos
        FiltroNominaDTO filtro = new FiltroNominaDTO();
        filtro.setFiltroNombre(filtroNombre);
        filtro.setFiltroMes(filtroMes);
        filtro.setFiltroAnio(filtroAnio);
        filtro.setTotalLiquidoMinimo(totalLiquidoMinimo);
        filtro.setTotalLiquidoMaximo(totalLiquidoMaximo);
        filtro.setConceptosSeleccionados(conceptosSeleccionados);

        // Obtener las nóminas filtradas con paginación
        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerNominasFiltradas(filtro, page);

        // Agregar los resultados al modelo
        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtro", filtro);


        model.addAttribute("filtroNombre", filtroNombre);
        model.addAttribute("filtroMes", filtroMes);
        model.addAttribute("filtroAnio", filtroAnio);
        model.addAttribute("filtroLiquidoMinimo", totalLiquidoMinimo);
        model.addAttribute("filtroLiquidoMaximo", totalLiquidoMaximo);
        model.addAttribute("conceptosSeleccionados", conceptosSeleccionados);
        return "listados/listado-vista-nomina";
    }


}
