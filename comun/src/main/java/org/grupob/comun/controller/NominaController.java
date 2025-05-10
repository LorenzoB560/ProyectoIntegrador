package org.grupob.comun.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.FiltroNominaDTO;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.dto.NominaDTO;
import org.grupob.comun.exception.NominaPasadaException;
import org.grupob.comun.service.NominaServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/nomina")
public class NominaController {

    private final NominaServiceImp nominaServiceImp;

    public NominaController(NominaServiceImp nominaServiceImp) {
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

        String appSource = (String) sesion.getAttribute("appSource");

        boolean esAdminApp = "AdminApp".equals(appSource);
        boolean esEmpApp = "EmpApp".equals(appSource);

        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");

        // Redirección adecuada según el módulo de origen
        if (esAdminApp && adminDTO == null) {
            return "redirect:/adminapp/login";
        } else if (esEmpApp && loginUsuarioEmpleadoDTO == null) {
            return "redirect:/empapp/login";
        } else if (esEmpApp) {
            return "redirect:/nomina/listado/" + loginUsuarioEmpleadoDTO.getId();
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

    @GetMapping("/listado/{id}")
    public String listarNominaEmpleado(@PathVariable UUID id,
                                @RequestParam(defaultValue = "0") int page,
                                Model model,
                                HttpSession sesion,
                                HttpServletRequest request) {



        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");

        if (loginUsuarioEmpleadoDTO == null) {
            return "redirect:/empapp/login";
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


    @GetMapping("/detalle/{id}")
    public String vistaDetalleNomina(@PathVariable UUID id, Model model) {

        //Obtengo el DTO de la nómina según el ID en el servicio, y se lo paso a la vista
        NominaDTO nominaDTO = nominaServiceImp.devolverNominaPorId(id);
        nominaDTO = nominaServiceImp.actualizarLiquidoTotal(nominaDTO);

        model.addAttribute("nominaDTO", nominaDTO);
        System.err.println(nominaDTO);
        return "listados/detalle-vista-nomina";
    }

    @GetMapping("/modificar/{id}")
    public String modificarNomina(@PathVariable UUID id, RedirectAttributes redirectAttributes, Model model) {
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
            @RequestParam(required = false) Integer filtroMes,
            @RequestParam(required = false) Integer filtroAnio,
            @RequestParam(required = false) BigDecimal totalLiquidoMin,
            @RequestParam(required = false) BigDecimal totalLiquidoMax,
            @RequestParam(required = false) List<String> conceptosSeleccionados,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        FiltroNominaDTO filtro = new FiltroNominaDTO();
        filtro.setFiltroNombre(filtroNombre);
        filtro.setFiltroMes(filtroMes);
        filtro.setFiltroAnio(filtroAnio);
        filtro.setTotalLiquidoMinimo(totalLiquidoMin);
        filtro.setTotalLiquidoMaximo(totalLiquidoMax);
        filtro.setConceptosSeleccionados(conceptosSeleccionados);

        Page<NominaDTO> paginaNominas = nominaServiceImp.obtenerNominasFiltradas(filtro, page);

        model.addAttribute("listaNominas", paginaNominas.getContent());
        model.addAttribute("totalPaginas", paginaNominas.getTotalPages());
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtro", filtro);

        model.addAttribute("filtroNombre", filtroNombre);
        model.addAttribute("filtroMes", filtroMes);
        model.addAttribute("filtroAnio", filtroAnio);
        model.addAttribute("filtroLiquidoMinimo", totalLiquidoMin);
        model.addAttribute("filtroLiquidoMaximo", totalLiquidoMax);
        model.addAttribute("conceptosSeleccionados", conceptosSeleccionados);

        // NUEVO
        model.addAttribute("modo", "parametrizada");

        StringBuilder queryString = new StringBuilder();
        if (filtroNombre != null && !filtroNombre.isBlank()) queryString.append("&filtroNombre=").append(filtroNombre);
        if (filtroMes != null) queryString.append("&filtroMes=").append(filtroMes);
        if (filtroAnio != null) queryString.append("&filtroAnio=").append(filtroAnio);
        if (totalLiquidoMin != null) queryString.append("&totalLiquidoMin=").append(totalLiquidoMin);
        if (totalLiquidoMax != null) queryString.append("&totalLiquidoMax=").append(totalLiquidoMax);
        if (conceptosSeleccionados != null) {
            for (String concepto : conceptosSeleccionados) {
                queryString.append("&conceptosSeleccionados=").append(concepto);
            }
        }
        model.addAttribute("queryString", queryString.toString());

        return "listados/listado-vista-nomina";
    }


}
