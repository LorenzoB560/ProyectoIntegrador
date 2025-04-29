package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarClaveDTO {
    @NotBlank(message = "{login.clave.nueva.requerida}")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\p{Punct}]).{8,12}$",
            message = "{error.clave.complejidad}")
    private String nuevaClave;

    @NotBlank(message = "{login.clave.confirmacion.requerida}")
    private String confirmacionClave;

    private String usuario;

    public ActualizarClaveDTO(String usuario) {
        this.usuario = usuario;
    }
}
