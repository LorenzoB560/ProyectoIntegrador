package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.grupo_validaciones.GrupoUsuario;
import org.grupob.empapp.validation.clave.ClaveCoincide;
import org.grupob.comun.validation.email.EmailValidado;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ClaveCoincide(groups = GrupoUsuario.class)
public class RegistroUsuarioEmpleadoDTO {
    private UUID id;

    @NotBlank(message = "El usuario no debe estar vacío", groups = GrupoUsuario.class)
    @EmailValidado(groups = GrupoUsuario.class)
    private String usuario;

    //VALIDAR PATRON (EJ: 8 CARACTERES, MINIMO UN NUMERO, MINIMO UN CARACTER ESPECIAL)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.,;:¿?¡!(){}])[A-Za-z\\d.,;:¿?¡!(){}]{8,}$",
            message = "Introduzca una contraseña válida.",
            groups = GrupoUsuario.class)
    // (?=.*[A-Za-z])         -- CONTIENE 1 LETRA (mayúscula o minúscula)
    // (?=.*\d)               -- CONTIENE 1 DIGITO [0-9]
    // (?=.*[.,;:¿?¡!(){}])   -- CONTIENE UNO DE LOS CARACTERES ESPECIALES DEL CORCHETE
    // [A-Za-z\d@$!%*#?&]{8,} -- MÍNIMO 8 CARACTERES, LETRAS DE LA A-Z, DÍGITOS, Y SÍMBOLOS
    private String clave;


    private String confirmarClave;
}
