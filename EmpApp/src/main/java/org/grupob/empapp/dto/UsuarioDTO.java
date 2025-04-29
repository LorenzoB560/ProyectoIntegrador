package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.validation.clave.ClaveCoincide;
import org.grupob.comun.validation.email.EmailValidado;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

//Validacion personalizada a nivel de clase de la contraseña
@ClaveCoincide
public class UsuarioDTO {

    private UUID id;

    @NotBlank
    //VALIDAR QUE SEA UN EMAIL CORRECTO (NO SIRVE @Email)
    @EmailValidado //Se ha creado validación personalizada
    private String nombreUsuario;

    //VALIDAR PATRON (EJ: 8 CARACTERES, MINIMO UN NUMERO, MINIMO UN CARACTER ESPECIAL)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Introduzca una contraseña válida.")
    // (?=.*[A-Za-z])         -- CONTIENE 1 LETRA (mayúscula o minúscula)
    // (?=.*\d)               -- CONTIENE 1 DIGITO [0-9]
    // (?=.*[@$!%*#?&])       -- CONTIENE UNO DE LOS CARACTERES ESPECIALES DEL CORCHETE
    // [A-Za-z\d@$!%*#?&]{8,} -- MÍNIMO 8 CARACTERES, LETRAS DE LA A-Z, DÍGITOS, Y SÍMBOLOS
    private String clave;

    //VALIDAR CON VALIDACION DE CLASE QUE LAS CONTRASEÑAS SEAN IGUALES

    private String confirmarClave;

    private LocalDateTime ultimoAcceso;


}
