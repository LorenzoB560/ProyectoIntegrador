package org.grupob.adminapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String concepto;  // Concepto de la línea (ej: salario, impuestos, etc.)
    private BigDecimal cantidad;  // Cantidad (positiva para ingresos, negativa para retenciones)

//    @ManyToOne()
//    @JoinColumn(name = "id_nomina", foreignKey = @ForeignKey(name = "FK_nomina_linea_nomina_id"))
//    private Nomina nomina;


}
