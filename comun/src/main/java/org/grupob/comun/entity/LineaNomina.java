package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.maestras.Concepto;

import java.math.BigDecimal;
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
    private Concepto concepto;  // Concepto de la línea (ej: salario, impuestos, etc.)

    private BigDecimal cantidad;  // Cantidad (positiva para ingresos, negativa para retenciones)

    public LineaNomina(Concepto concepto, BigDecimal importe) {
        setConcepto(concepto);
        setCantidad(importe);
    }

}
