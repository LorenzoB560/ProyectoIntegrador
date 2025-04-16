package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table()
public class Nomina {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer mes;
    private Integer annio;
    private BigDecimal totalLiquido;

    @OneToMany
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_nomina_linea_id"))
    private Set<LineaNomina> lineaNominas = new HashSet<>();
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_nomina_empleado_id"))
    private Empleado empleado;

    public Nomina(Integer mes, Integer annio){
        setMes(mes);
        setAnnio(annio);
    }


}

