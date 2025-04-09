package org.grupob.proyectointegrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.proyectointegrador.entity.auxiliar.DireccionPostal;
import org.grupob.proyectointegrador.entity.auxiliar.Periodo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleado")
@DiscriminatorValue("EMPLEADO")
public class Administrador extends Persona {

    @Column(name = "telefono_movil")
    private String telefonoMovil;

    @Embedded
    private DireccionPostal direccion;

    @Embedded
    private Periodo periodo;

    // Relación empleado-jefe
    @ManyToOne
    @JoinColumn(name = "id_jefe",
            foreignKey = @ForeignKey(name = "FK_empleado_empleado_id_jefe"))
    private Empleado jefe;



    // Relación empleado-departamento
    @ManyToOne
    @JoinColumn(name = "id_departamento",
            foreignKey = @ForeignKey(name = "FK_empleado_departamento_id_departamento"))
    private Departamento departamento;

//    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Usuario usuario;


}
