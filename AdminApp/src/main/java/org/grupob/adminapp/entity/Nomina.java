package org.grupob.adminapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.Empleado;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


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

