package org.grupob.proyectointegrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "nomina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nomina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "Mes", nullable = false)
    private YearMonth mes;

    @Column(name = "Año", nullable = false)
    private Integer year;


    // Add explicit year column
    @Column(name = "anio", nullable = false)
    private Integer anio;



    @Column(name = "liquido", nullable = false)
    private BigDecimal liquido;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false,
            foreignKey = @ForeignKey(name = "FK_nomina_empleado_id_empleado"))
    private Empleado empleado;

    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaNomina> lineas = new ArrayList<>();



}
