package org.grupob.proyectointegrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "linea_nomina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaNomina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "concepto", nullable = false)
    private String concepto;


}
