package org.grupob.empapp.controller;

import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroUsuarioController {

    @GetMapping("/registro-usuario")
    public String login(Model modelo) {
        RegistroUsuarioEmpleadoDTO datosFormulario = new RegistroUsuarioEmpleadoDTO();
        modelo.addAttribute("datos", datosFormulario);

        return "registro_usuario/registro";
    }

    @GetMapping("/redirigir-hacia-login")
    public String redirigirLogin() {
        return "redirect:/empapp/login";
    }
}
