package org.grupob.proyectointegrador.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private UUID id;

    @NotBlank
    //VALIDAR QUE SEA UN EMAIL CORRECTO (NO SIRVE @Email)
    private String nombreUsuario;

    //VALIDAR PATRON (EJ: 8 CARACTERES, MINIMO UN NUMERO, MINIMO UN CARACTER ESPECIAL)
    private String contrasena;

    //VALIDAR CON VALIDACION DE CLASE QUE LAS CONTRASEÑAS SEAN IGUALES
    private String confirmacionContrasena;

    private LocalDateTime ultimoAcceso;


}
