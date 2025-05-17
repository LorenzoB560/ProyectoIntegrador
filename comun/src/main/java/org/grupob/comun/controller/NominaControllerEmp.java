package org.grupob.comun.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.*;
import org.grupob.comun.exception.NominaPasadaException;
import org.grupob.comun.service.NominaServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/nomina")
public class NominaControllerEmp {

    private final NominaServiceImp nominaServiceImp;

    public NominaControllerEmp (NominaServiceImp nominaServiceImp) {
        this.nominaServiceImp = nominaServiceImp;
    }

    @ModelAttribute
    public void adicionDatosModel(Model modelo) {
        modelo.addAttribute("meses", nominaServiceImp.devolverMeses());
        modelo.addAttribute("anios", nominaServiceImp.devolverAnios());
        modelo.addAttribute("listaNominas", nominaServiceImp.devolverNominas());
        modelo.addAttribute("listaConceptos", nominaServiceImp.devolverConceptos());
        modelo.addAttribute("listaPropiedades", nominaServiceImp.devolverPropiedades());
    }

    @GetMapping("/listado/{id}")
    public String listarNominaEmpleado(@PathVariable UUID id,
                                       @RequestParam(defaultValue = "0") int page,
                                       Model model,
                                       HttpSession sesion) {


        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");

        if (loginUsuarioEmpleadoDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioDTO", loginUsuarioEmpleadoDTO);
        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerNominasEmpleado(page, 10, id); // 10 por página
        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", paginaNominas.getNumber());

        model.addAttribute("modo", "listado");
        model.addAttribute("queryString", "");

        return "listados/listado-vista-nomina-empleado";
    }


    @GetMapping("/detalle-empleado/{id}")
    public String vistaDetalleNominaEmpleado(@PathVariable UUID id, Model model, HttpSession sesion) {

        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (loginUsuarioEmpleadoDTO == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", loginUsuarioEmpleadoDTO);

        //Obtengo el DTO de la nómina según el ID en el servicio, y se lo paso a la vista
        NominaDTO nominaDTO = nominaServiceImp.devolverNominaPorId(id);
        nominaDTO = nominaServiceImp.actualizarLiquidoTotal(nominaDTO);
        model.addAttribute("nominaDTO", nominaDTO);

        BigDecimal totalIngresos = nominaDTO.getLineaNominas().stream()
                .filter(c -> "INGRESO".equals(c.getTipoConcepto()))
                .map(LineaNominaDTO::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BigDecimal totalDeducciones = nominaDTO.getLineaNominas().stream()
                .filter(c -> "DEDUCCION".equals(c.getTipoConcepto()))
                .map(LineaNominaDTO::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalIngresos", totalIngresos);
        model.addAttribute("totalDeducciones", totalDeducciones);
        nominaServiceImp.asignarDatosComunesNomina(id, model);

        model.addAttribute("nominaDTO", nominaDTO);
        System.err.println(nominaDTO);
        return "listados/detalle-vista-nomina-emp";
    }

    @GetMapping("/busqueda-parametrizada-empleado")
    public String listarNominaEmpleadoConFitlro(@RequestParam(required = false) String fechaInicio,
                                                @RequestParam(required = false) String fechaFin,
                                                @RequestParam(defaultValue = "0") int page,
                                                Model model,
                                                HttpSession sesion,
                                                HttpServletRequest request
    ) {
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (loginUsuarioEmpleadoDTO == null) {
            return "redirect:/empapp/login";
        }
        model.addAttribute("usuarioDTO", loginUsuarioEmpleadoDTO);

        FiltroNominaEmpleadoDTO filtro = new FiltroNominaEmpleadoDTO();
        filtro.setIdEmpleado(loginUsuarioEmpleadoDTO.getId());
        // Convertir fechas manualmente
        try {
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                filtro.setFechaInicio(LocalDate.parse(fechaInicio));
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                filtro.setFechaFin(LocalDate.parse(fechaFin));
            }
        } catch (DateTimeParseException e) {
            // Si hay error de formato, continuar sin fechas
            model.addAttribute("errorFecha", "Formato de fecha incorrecto. Use YYYY-MM-DD");
        }

        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerNominasFiltradasPorEmpleado(filtro, page);

        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtro", filtro);

        model.addAttribute("idEmpleado", loginUsuarioEmpleadoDTO.getId());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        model.addAttribute("modo", "parametrizada");

        StringBuilder queryString = new StringBuilder();
        if (fechaInicio != null && !fechaInicio.isEmpty()) queryString.append("&fechaInicio=").append(fechaInicio);
        if (fechaFin != null && !fechaFin.isEmpty()) queryString.append("&fechaFin=").append(fechaFin);
        model.addAttribute("queryString", queryString.toString());
        return "listados/listado-vista-nomina-empleado";
    }



}
