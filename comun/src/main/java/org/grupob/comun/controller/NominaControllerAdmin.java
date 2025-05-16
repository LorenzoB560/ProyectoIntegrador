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
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/nomina")
public class NominaControllerAdmin {

    private final NominaServiceImp nominaServiceImp;

    public NominaControllerAdmin(NominaServiceImp nominaServiceImp) {
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
    public String listarNominas(@RequestParam(defaultValue = "0") int page, Model model, HttpSession sesion, HttpServletRequest request) {

        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        String redireccion = nominaServiceImp.gestionarAccesoYRedireccion(adminDTO, loginUsuarioEmpleadoDTO, sesion, model, request);
        if (redireccion != null) {
            return redireccion;
        }

        model.addAttribute("adminDTO", adminDTO);

        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerTodasNominasPaginadas(page, 10); // 10 por página
        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", paginaNominas.getNumber());
        model.addAttribute("modo", "listado");
        model.addAttribute("queryString", "");

        return "listados/listado-vista-nomina";
    }

    @GetMapping("/detalle/{id}")
    public String vistaDetalleNominaAdmin(@PathVariable UUID id, Model model, HttpSession sesion, HttpServletRequest request) {

        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        String redireccion = nominaServiceImp.gestionarAccesoYRedireccion(adminDTO, loginUsuarioEmpleadoDTO, sesion, model, request);
        if (redireccion != null) {
            return redireccion;
        }

        model.addAttribute("adminDTO", adminDTO);
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
        model.addAttribute("empleadoNominaDTO", nominaServiceImp.devolverEmpleadoPorId(id));

        System.err.println(nominaDTO);
        return "listados/detalle-vista-nomina-admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificarNomina(@PathVariable UUID id, RedirectAttributes redirectAttributes, Model model, HttpSession sesion, HttpServletRequest request) {


        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        String redireccion = nominaServiceImp.gestionarAccesoYRedireccion(adminDTO, loginUsuarioEmpleadoDTO, sesion, model, request);
        if (redireccion != null) {
            return redireccion;
        }

        model.addAttribute("adminDTO", adminDTO);
        try {
            NominaDTO nominaDTO = nominaServiceImp.devolverNominaPorId(id);

            // Verificar si la nómina pertenece a un mes anterior
            nominaServiceImp.verificarNominaPasada(nominaDTO);

            model.addAttribute("nominaDTO", nominaDTO);
            return "nomina/modificar-vista-nomina";

        } catch (NominaPasadaException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Envía el mensaje de error a la vista
            return "redirect:/nomina/listado"; // Redirige al listado de nóminas
        }
    }

    @PostMapping("/guardar-datos-modificados")
    public String guardarDatosModificados(@ModelAttribute NominaDTO nominaDTO, Model model) {

        System.out.println(nominaDTO);
        nominaServiceImp.modificarNomina(nominaDTO);
        return "redirect:/nomina/detalle/" + nominaDTO.getId();
    }


    @GetMapping("/busqueda-parametrizada")
    public String listarNominasConFiltros(
            @RequestParam(required = false) String filtroNombre,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) List<String> conceptosSeleccionados,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpSession sesion,
            HttpServletRequest request
    ) {

        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        String redireccion = nominaServiceImp.gestionarAccesoYRedireccion(adminDTO, loginUsuarioEmpleadoDTO, sesion, model, request);
        if (redireccion != null) {
            return redireccion;
        }
        model.addAttribute("adminDTO", adminDTO);

        FiltroNominaDTO filtro = new FiltroNominaDTO();
        filtro.setFiltroNombre(filtroNombre);
        filtro.setFechaInicio(fechaInicio);
        filtro.setFechaFin(fechaFin);
        filtro.setConceptosSeleccionados(conceptosSeleccionados);

        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerNominasFiltradas(filtro, page);

        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtro", filtro);

        model.addAttribute("filtroNombre", filtroNombre);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("conceptosSeleccionados", conceptosSeleccionados);

        model.addAttribute("modo", "parametrizada");

        StringBuilder queryString = new StringBuilder();
        if (filtroNombre != null && !filtroNombre.isBlank()) queryString.append("&filtroNombre=").append(filtroNombre);
        if (fechaInicio != null) queryString.append("&fechaInicio=").append(fechaInicio);
        if (fechaFin != null) queryString.append("&fechaFin=").append(fechaFin);
        if (conceptosSeleccionados != null) {
            for (String concepto : conceptosSeleccionados) {
                queryString.append("&conceptosSeleccionados=").append(concepto);
            }
        }
        model.addAttribute("queryString", queryString.toString());
        return "listados/listado-vista-nomina";
    }

}
