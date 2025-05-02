package org.grupob.adminapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.adminapp.validation.contrasena.ContrasenaCoincide;
import org.grupob.comun.validation.email.EmailValidado;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginAdministradorDTO {

    private UUID id;

    @NotBlank
    @EmailValidado
    private String usuario;

    @NotBlank
    private String clave;

    private Integer numeroAccesos;

    public LoginAdministradorDTO(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }
}
