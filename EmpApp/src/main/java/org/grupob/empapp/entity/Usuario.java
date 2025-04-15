package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Transient
    private String confirmacionContrasena;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @OneToOne
    @JoinColumn(name = "id_empleado", unique = true,
            foreignKey = @ForeignKey(name = "FK_usuario_empleado_id_empleado"))
    private Empleado empleado;

}
