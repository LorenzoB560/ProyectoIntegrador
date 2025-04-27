package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.validation.email.EmailValidado;
import org.grupob.empapp.dto.grupoValidaciones.GrupoClave;
import org.grupob.empapp.dto.grupoValidaciones.GrupoUsuario;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUsuarioEmpleadoDTO {
    private UUID id;

    @NotBlank(groups = GrupoUsuario.class)
    @EmailValidado(groups = GrupoUsuario.class)
    private String usuario;

    @NotBlank(groups = GrupoClave.class)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[.,;:¿?¡!(){}])[A-Za-z\\d.,;:¿?¡!(){}]{8,12}$",
            message = """
                    Introduzca una contraseña válida. Debe tener una longitud de entre 8 y 12 caracteres.\s
                     - Debe contener, al menos, una letra mayúscula, una minúscula, un número y un\s
                    signo de puntuación.""",
            groups = GrupoClave.class)
    private String clave;

    private Integer numeroAccesos;

    private LocalDateTime ultimaConexion;

    private String motivoBloqueo;

    private String mensajeBloqueo;

    private LocalDateTime fechaDesbloqueo;

    private Integer intentosSesionFallidos;

    public LoginUsuarioEmpleadoDTO(String usuario, String clave){
        this.usuario=usuario;
        this.clave=clave;
    }
}
