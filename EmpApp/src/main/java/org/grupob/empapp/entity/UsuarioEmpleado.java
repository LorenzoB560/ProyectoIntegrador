package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.Usuario;
import org.grupob.empapp.entity.maestras.MotivoBloqueo;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UsuarioEmpleado extends Usuario {

    @OneToOne
    @JoinColumn(name = "id_empleado", unique = true,
            foreignKey = @ForeignKey(name = "FK_usuario_empleado_id"))
    private Empleado empleado;

    @Column(name = "ultima_conexion")
    private LocalDateTime ultimaConexion;
    @Column(name = "num_accesos")
    private Integer numeroAccesos;

    private Boolean activo;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_usuario_empleado_motivo_bloqueo_id"))
    private MotivoBloqueo motivoBloqueo;

    @Column(name = "fecha_desbloqueo")
    private LocalDateTime fechaDesbloqueo;
    @Column(name = "intentos_sesion_fallidos")
    private Integer intentosSesionFallidos;

    public UsuarioEmpleado(String email, String clave) {
        super(email, clave);
    }
}
