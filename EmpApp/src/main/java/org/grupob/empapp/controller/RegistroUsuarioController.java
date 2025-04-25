package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.grupoValidaciones.GrupoPersonal;
import org.grupob.empapp.dto.grupoValidaciones.GrupoUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
