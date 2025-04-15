package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "departamento", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_departamento_codigo", columnNames = "codigo"),
        @UniqueConstraint(name = "UQ_departamento_id_jefe", columnNames = "id_jefe"),
})
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "codigo", unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "presupuesto", nullable = false)
    private BigDecimal presupuesto;


    @OneToMany(mappedBy = "departamento")
    private List<Empleado> empleados = new ArrayList<>();
}
