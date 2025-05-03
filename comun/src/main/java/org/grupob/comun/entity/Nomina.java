package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table()
public class Nomina {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_nomina")
    private UUID id;

    private Integer mes;
    private Integer anio;
    private BigDecimal totalLiquido;

    @OneToMany
    @JoinColumn(name = "id_nomina", foreignKey = @ForeignKey(name = "FK_nomina_linea_id"))
    private Set<LineaNomina> lineaNominas = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_nomina_empleado_id"))
    private Empleado empleado;

    public Nomina(Integer mes, Integer annio){
        setMes(mes);
        setAnio(annio);
    }


}

