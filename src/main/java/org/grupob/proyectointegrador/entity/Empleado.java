package org.grupob.proyectointegrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.proyectointegrador.entity.auxiliar.DireccionPostal;

import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleado")
@DiscriminatorValue("EMPLEADO")
public class Empleado extends Persona {

    @Column(name = "telefono_movil")
    private String telefonoMovil;

    @Embedded
    private DireccionPostal direccion;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "fecha_cese")
    private LocalDate fechaCese;




    // Relación empleado-jefe
    @ManyToOne
    @JoinColumn(name = "id_jefe",
            foreignKey = @ForeignKey(name = "FK_empleado_empleado_id_jefe"))
    private Empleado jefe;

    @OneToMany(mappedBy = "jefe")
    private List<Empleado> subordinados = new ArrayList<>();

    // Relación empleado-departamento
    @ManyToOne
    @JoinColumn(name = "id_departamento",
            foreignKey = @ForeignKey(name = "FK_empleado_departamento_id_departamento"))
    private Departamento departamento;

    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;

    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private InformacionEconomica informacionEconomica;


    @Column(name = "fecha_eliminacion")
    private LocalDate fechaEliminacion;

    @Column(name = "fecha_insercion")
    private LocalDate fechaInsercion;

}
