package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.validation.contrasena.ClaveCoincide;
import org.grupob.empapp.validation.email.EmailValidado;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Autentica {

    @EmailValidado
    private String usuario;
    @NotBlank
    private String clave;
    private String confirmarClave;
}
