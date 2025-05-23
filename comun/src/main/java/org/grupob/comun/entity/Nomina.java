package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.Periodo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
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


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fechaInicio", column = @Column(name = "fecha_inicio")),
            @AttributeOverride(name = "fechaFin", column = @Column(name = "fecha_fin")),
    })
    private Periodo periodo;
    private BigDecimal totalLiquido;

    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LineaNomina> lineaNominas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_nomina_empleado_id"))
    private Empleado empleado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nomina nomina)) return false;
        return id != null && id.equals(nomina.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}