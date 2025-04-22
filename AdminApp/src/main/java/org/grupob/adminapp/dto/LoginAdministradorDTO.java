package org.grupob.adminapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.adminapp.validation.email.EmailValidado;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@Validated
public class LoginAdministradorDTO {

    private UUID id;

    @NotBlank
    @EmailValidado
    private String correo;

    @NotBlank
    private String clave;

    private Integer numeroAccesos;
}
