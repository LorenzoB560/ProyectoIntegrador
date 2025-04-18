package org.grupob.empapp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.maestras.MotivoBloqueo;
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

    private Boolean bloqueado;

    private String motivoBloqueo;

    private String mensajeBloqueo;

    private LocalDateTime fechaDesbloqueo;

    private Integer intentosSesionFallidos;
}
