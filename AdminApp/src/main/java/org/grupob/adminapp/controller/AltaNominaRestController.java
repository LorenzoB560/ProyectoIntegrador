package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/adminapp")
public class AltaNominaRestController {

    private final AltaNominaServiceImp altaNominaServiceImp;

    public AltaNominaRestController(AltaNominaServiceImp altaNominaServiceImp) {
        this.altaNominaServiceImp = altaNominaServiceImp;
    }

    @PostMapping("/guardar-nomina")
    public ResponseEntity<?> crearNomina(@RequestBody AltaNominaDTO altaNominaDTO) {

        altaNominaServiceImp.guardarNomina(altaNominaDTO);
        return ResponseEntity.ok(altaNominaDTO);
    }
}
