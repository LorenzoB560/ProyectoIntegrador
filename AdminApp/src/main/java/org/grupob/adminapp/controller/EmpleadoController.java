package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.service.EmpleadoServiceImp;
import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.grupob.comun.repository.maestras.MotivoBloqueoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("empleado")
public class EmpleadoController {

    @Autowired
    private MotivoBloqueoRepository motivoBloqueoRepository;
    @Autowired
    private EmpleadoServiceImp empleadoService;

    @GetMapping("lista")
    public String listadovista(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if(adminDTO==null){
            return "redirect:/adminapp/login";
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "listados/listado-vista-emp";
    }
    @GetMapping("detalle/{id}")
    public String listadoEmpleadoVista(@PathVariable String id, Model modelo, HttpSession sesion){
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if(adminDTO==null){
            return "redirect:/adminapp/login";
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "listados/detalle-vista-emp";
    }
    @GetMapping("/{id}/bloquear/motivos")
    public String mostrarFormularioBloqueo(@PathVariable String id, Model model, HttpSession session) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) session.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/adminapp/login";
        }
        try {
            EmpleadoDTO empleado = empleadoService.devuelveEmpleado(id);
            List<MotivoBloqueo> motivos = motivoBloqueoRepository.findAll();
            model.addAttribute("empleado", empleado);
            model.addAttribute("motivosBloqueo", motivos);
            model.addAttribute("loginAdminDTO", adminDTO); // Para el layout
            return "listados/seleccionar-motivo-bloqueo"; // Nombre de la nueva vista
        } catch (Exception e) {
            // Manejar empleado no encontrado
            return "redirect:/empleado/lista?error=Empleado no encontrado";
        }
    }

}
