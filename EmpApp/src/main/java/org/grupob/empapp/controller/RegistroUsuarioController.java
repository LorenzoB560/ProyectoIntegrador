package org.grupob.empapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistroUsuarioController {

    @GetMapping("/login")
    public String login() {


        return "registro_usuario/registro";
    }
    @PostMapping("/guardar-login")
    public String guardarLogin() {
        return "registro_usuario/registro";
    }
}
