package org.grupob.empapp.controller;

import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.comun.dto.grupo_validaciones.GrupoUsuario;
import org.grupob.empapp.service.RegistroUsuarioServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        registroUsuarioServiceImp.guardarUsuario(datosFormulario);

        return ResponseEntity.ok(datosFormulario);
    }

}
