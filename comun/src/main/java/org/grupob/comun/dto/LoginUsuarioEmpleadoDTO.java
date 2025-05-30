package org.grupob.comun.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.validation.email.EmailValidado;
import org.grupob.comun.dto.grupo_validaciones.GrupoClave;
import org.grupob.comun.dto.grupo_validaciones.GrupoUsuario;


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
