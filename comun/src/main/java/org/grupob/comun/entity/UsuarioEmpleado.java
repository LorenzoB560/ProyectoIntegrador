package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.jerarquia.Usuario;
import org.grupob.comun.entity.maestras.MotivoBloqueo;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuario_empleado", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_usuario_empleado_usuario", columnNames = "usuario"),
        @UniqueConstraint(name = "UQ_usuario_empleado", columnNames = "id_empleado")})
public class UsuarioEmpleado extends Usuario {

//    @OneToOne
//    @JoinColumn(name = "id_empleado", unique = true,
//            foreignKey = @ForeignKey(name = "FK_usuario_empleado_id"))
//    private Empleado empleado;

    @Column(name = "ultima_conexion")
    private LocalDateTime ultimaConexion;


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
        setActivo(true);
        setIntentosSesionFallidos(0);
    }
}
