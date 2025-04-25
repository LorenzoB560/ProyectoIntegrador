package org.grupob.comun.entity.maestras;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
