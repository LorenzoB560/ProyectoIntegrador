package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.Periodo;
import org.grupob.empapp.entity.auxiliar.Persona;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleado")

public class Administrador extends Persona {

    @Column(name = "telefono_movil")
    private String telefonoMovil;


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
