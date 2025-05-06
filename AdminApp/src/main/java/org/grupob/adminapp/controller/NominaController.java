package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.adminapp.service.NominaServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/nomina")
public class NominaController {

    private final NominaServiceImp nominaServiceImp;

    public NominaController(NominaServiceImp nominaServiceImp, AltaNominaServiceImp altaNominaServiceImp) {
        this.nominaServiceImp = nominaServiceImp;
    }


    @GetMapping("/listado")
    public String listarNominas(Model modelo) {

        List<NominaDTO> listaNominas = nominaServiceImp.devolverNominas();
        modelo.addAttribute("listaNominas", listaNominas);
        return "listados/listado-vista-nomina";
    }

    @GetMapping("/detalle/{id}")
    public String vistaDetalleProducto(@PathVariable UUID id, Model model) {

        //Obtengo el DTO de la nómina según el ID en el servicio, y se lo paso a la vista
        model.addAttribute("nominaDTO", nominaServiceImp.devolverNominaPorId(id));

        return "listados/detalle-vista-nomina";
    }

}
