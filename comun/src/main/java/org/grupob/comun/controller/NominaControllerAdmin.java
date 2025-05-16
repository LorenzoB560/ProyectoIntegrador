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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        modelo.addAttribute("listaPropiedades", nominaServiceImp.devolverPropiedades());
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
        if (adminDTO == null){
            return "redirect:/adminapp/login";
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
        asignarDatosComunesNomina(id, model);


        System.err.println(nominaDTO);
        return "listados/detalle-vista-nomina-admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificarNomina(@PathVariable UUID id, RedirectAttributes redirectAttributes, Model model, HttpSession sesion, HttpServletRequest request) {


        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if (adminDTO == null) {
            return "redirect:/adminapp/login";
        }

        model.addAttribute("adminDTO", adminDTO);
        asignarDatosComunesNomina(id, model);

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
    public String guardarDatosModificados(@ModelAttribute NominaDTO nominaDTO,
                                          @RequestParam("periodo.fechaInicio") String fechaInicioStr,
                                          @RequestParam("periodo.fechaFin") String fechaFinStr) {

        List<LineaNominaDTO> lineaNominaDTOS = nominaDTO.getLineaNominas();

        if (lineaNominaDTOS != null && !lineaNominaDTOS.isEmpty()) {
            LineaNominaDTO salarioBase = lineaNominaDTOS.getFirst();
            salarioBase.setIdConcepto(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            salarioBase.setNombreConcepto("Salario base");
            salarioBase.setTipoConcepto("INGRESO");
            salarioBase.setCantidad(BigDecimal.valueOf(2800.00));
            salarioBase.setPorcentaje(null);
        } else {
            throw new IllegalStateException("No existen líneas de nómina en la lista.");
        }

        // Convertir las fechas manualmente
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy");
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

            if (nominaDTO.getPeriodo() == null) {
                // Crear un nuevo objeto periodo si es null
                PeriodoDTO periodoDTO = new PeriodoDTO();
                periodoDTO.setFechaInicio(fechaInicio);
                periodoDTO.setFechaFin(fechaFin);
                nominaDTO.setPeriodo(periodoDTO);
            } else {
                nominaDTO.getPeriodo().setFechaInicio(fechaInicio);
                nominaDTO.getPeriodo().setFechaFin(fechaFin);
            }
        } catch (Exception e) {
            // Manejar errores de formato de fecha
            e.printStackTrace();
            // Redirigir a una página de error o volver al formulario con mensaje de error
        }

        System.out.println(nominaDTO);
        nominaServiceImp.modificarNomina(nominaDTO);
        return "redirect:/nomina/detalle/" + nominaDTO.getId();
    }


    @GetMapping("/busqueda-parametrizada")
    public String listarNominasConFiltros(
            @RequestParam(required = false) String filtroNombre,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
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

        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerNominasFiltradas(filtro, page);

        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtro", filtro);

        model.addAttribute("filtroNombre", filtroNombre);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        model.addAttribute("modo", "parametrizada");

        StringBuilder queryString = new StringBuilder();
        if (filtroNombre != null && !filtroNombre.isBlank()) queryString.append("&filtroNombre=").append(filtroNombre);
        if (fechaInicio != null && !fechaInicio.isEmpty()) queryString.append("&fechaInicio=").append(fechaInicio);
        if (fechaFin != null && !fechaFin.isEmpty()) queryString.append("&fechaFin=").append(fechaFin);
        model.addAttribute("queryString", queryString.toString());
        return "listados/listado-vista-nomina";
    }

    private void asignarDatosComunesNomina(@PathVariable UUID id, Model model) {
        model.addAttribute("empleadoNominaDTO", nominaServiceImp.devolverEmpleadoPorIdNomina(id));
        BigDecimal brutoTotal = nominaServiceImp.devolverCantidadBrutaAcumulada(id);
        BigDecimal retencionesTotales = nominaServiceImp.devolverRetencionesAcumuladas(id);
        model.addAttribute("brutoTotal", brutoTotal);
        model.addAttribute("retencionesTotales", retencionesTotales);
        model.addAttribute("sumaLiquidoTotal", brutoTotal.subtract(retencionesTotales));
    }

}
