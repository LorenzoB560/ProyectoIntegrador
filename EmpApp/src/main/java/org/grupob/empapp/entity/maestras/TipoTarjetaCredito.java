package org.grupob.empapp.entity.maestras;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.Empleado;
import org.grupob.empapp.entity.Nomina;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TipoTarjetaCredito {

    @Id
    private Long id;

    @Column(name = "tipo_tarjeta")
    private String tipoTarjetaCredito;
}
