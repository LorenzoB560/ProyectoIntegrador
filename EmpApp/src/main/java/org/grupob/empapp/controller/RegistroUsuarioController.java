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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistroUsuarioController {

    @GetMapping("/choclo")
    public String login(Model modelo) {
        RegistroUsuarioEmpleadoDTO datosFormulario = new RegistroUsuarioEmpleadoDTO();
        modelo.addAttribute("datos", datosFormulario);

        return "registro_usuario/registro";
    }
    @PostMapping("/guardar-login")
    public String guardarLogin(
            //// ES ESTO LO QUE DA ERROR. LOS GRUPOS
            @Validated(GrupoUsuario.class) @ModelAttribute("datos") RegistroUsuarioEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registro_usuario/registro";
        }

        System.out.println(datosFormulario);
        return "registro_usuario/registro";
    }
}
