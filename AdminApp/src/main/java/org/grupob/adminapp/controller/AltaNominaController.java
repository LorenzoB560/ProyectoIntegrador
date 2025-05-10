package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.adminapp.service.AltaNominaService;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.EmpleadoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/nomina")
public class AltaNominaController {

    private final AltaNominaServiceImp altaNominaServiceImp;

    public AltaNominaController(AltaNominaServiceImp altaNominaServiceImp) {
        this.altaNominaServiceImp = altaNominaServiceImp;
    }


    @ModelAttribute
    public void cargaModelo(Model modelo) {

        modelo.addAttribute("listaEmpleados", altaNominaServiceImp.devuelveEmpleados());
        modelo.addAttribute("meses", altaNominaServiceImp.devolverMeses());
        modelo.addAttribute("anios", altaNominaServiceImp.devolverAnios());
        modelo.addAttribute("listaConceptos", altaNominaServiceImp.devolverConcepto());
    }

    @GetMapping("/alta")
    public String altaNomina(Model model, HttpSession sesion) {


        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");

        // Redirección adecuada según el módulo de origen
        if (adminDTO == null) {
            return "redirect:/adminapp/login";
        }

        model.addAttribute("loginAdminDTO", adminDTO);
        AltaNominaDTO altaNominaDTO = new AltaNominaDTO();
        model.addAttribute("datos", altaNominaDTO);
        return "nomina/alta-nomina";
    }
}
