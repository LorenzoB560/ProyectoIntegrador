package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.validation.email.EmailValidado;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUsuarioEmpleadoDTO {
    private UUID id;

    @NotBlank
    @EmailValidado
    private String correo;

    private String clave;

    private Integer numeroAccesos;

    private LocalDateTime ultimaConexion;

    private String motivoBloqueo;

    private String mensajeBloqueo;

    private LocalDateTime fechaDesbloqueo;

    private Integer intentosSesionFallidos;

    public LoginUsuarioEmpleadoDTO(String correo, String clave){
        this.correo=correo;
        this.clave=clave;
    }
}
