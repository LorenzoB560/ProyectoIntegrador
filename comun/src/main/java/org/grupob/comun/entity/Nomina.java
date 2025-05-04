package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nomina")
public class Nomina {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_nomina")
    private UUID id;

    private Integer mes;
    private Integer anio;
    private BigDecimal totalLiquido;

    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LineaNomina> lineaNominas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_nomina_empleado_id"))
    private Empleado empleado;

    // añadir línea de nómina
    public void addLineaNomina(LineaNomina lineaNomina) {
        lineaNominas.add(lineaNomina);
        lineaNomina.setNomina(this);
    }

    // eliminar línea de nómina
    public void removeLineaNomina(LineaNomina lineaNomina) {
        lineaNominas.remove(lineaNomina);
        lineaNomina.setNomina(null);
    }
}