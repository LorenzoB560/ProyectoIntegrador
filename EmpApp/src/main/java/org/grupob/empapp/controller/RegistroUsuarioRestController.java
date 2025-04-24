package org.grupob.empapp.controller;

import jakarta.validation.Valid;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.grupoValidaciones.GrupoUsuario;
import org.grupob.empapp.service.RegistroUsuarioServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistroUsuarioRestController {

    RegistroUsuarioServiceImp registroUsuarioServiceImp;

    public RegistroUsuarioRestController(RegistroUsuarioServiceImp registroUsuarioServiceImp) {
        this.registroUsuarioServiceImp = registroUsuarioServiceImp;
    }

    @PostMapping("/guardar-registro-usuario")
    public ResponseEntity<?> guardarLogin(@Validated(GrupoUsuario.class) @RequestBody RegistroUsuarioEmpleadoDTO datosFormulario) {
        System.out.println(datosFormulario);
        registroUsuarioServiceImp.usuarioExiste(datosFormulario);
        return ResponseEntity.ok(datosFormulario);
    }

}
