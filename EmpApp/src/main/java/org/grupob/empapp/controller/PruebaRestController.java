package org.grupob.empapp.controller;

import jakarta.validation.Valid;
import org.grupob.empapp.dto.Autentica;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaRestController {

    @PostMapping("/autentica")
    public Autentica recibeCredenciales(@Valid @RequestBody Autentica credenciales) {
        System.err.println(credenciales);

        return credenciales;
    }
}
