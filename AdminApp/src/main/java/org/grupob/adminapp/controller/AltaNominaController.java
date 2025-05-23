package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.entity.maestras.Concepto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/nomina")
@RequiredArgsConstructor
public class AltaNominaController {

    private final AltaNominaServiceImp altaNominaServiceImp;

    @ModelAttribute
    public void cargaModelo(Model modelo) {
        List<Concepto> conceptos = altaNominaServiceImp.devolverConcepto();


        modelo.addAttribute("meses", altaNominaServiceImp.devolverMeses());
        modelo.addAttribute("anios", altaNominaServiceImp.devolverAnios());
        modelo.addAttribute("listaEmpleados", altaNominaServiceImp.devuelveEmpleados());

        modelo.addAttribute("listaConceptos", conceptos);
        modelo.addAttribute("salarioBase", altaNominaServiceImp.devolverSalarioBase(conceptos));
        modelo.addAttribute("conceptosRestantes", altaNominaServiceImp.devolverConceptosRestantes(conceptos));
    }

    @GetMapping("/alta")
    public String altaNomina(Model model, HttpSession sesion) {


        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");

        // Redirección adecuada según el módulo de origen
        if (adminDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginAdminDTO", adminDTO);
        AltaNominaDTO altaNominaDTO = new AltaNominaDTO();
        model.addAttribute("datos", altaNominaDTO);
        return "nomina/alta-nomina";
    }
}
