package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.maestras.Concepto;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "linea_nomina")
public class LineaNomina {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_concepto", nullable = false)
    private Concepto concepto;

    private BigDecimal porcentaje;
    private BigDecimal cantidad;

    @ManyToOne
    @JoinColumn(name = "id_nomina")
    private Nomina nomina;

    // Constructor con parámetros útiles
    public LineaNomina(Concepto concepto, BigDecimal cantidad) {
        this.concepto = concepto;
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaNomina that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}