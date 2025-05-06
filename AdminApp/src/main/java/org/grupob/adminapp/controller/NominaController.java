package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.adminapp.service.NominaServiceImp;
import org.grupob.comun.entity.Nomina;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/nomina")
public class NominaController {

    private final NominaServiceImp nominaServiceImp;

    public NominaController(NominaServiceImp nominaServiceImp) {
        this.nominaServiceImp = nominaServiceImp;
    }


    @GetMapping("/listado")
    public String listarNominas(Model modelo) {

        List<NominaDTO> listaNominas = nominaServiceImp.devolverNominas();
        modelo.addAttribute("listaNominas", listaNominas);
        return "nomina/listado-vista-nomina";
    }

}
