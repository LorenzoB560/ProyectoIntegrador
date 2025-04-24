package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarjetaCredito {
    private String numero;

    @Column(name = "mes_caducidad")
    private String mesCaducidad;

    @Column(name = "anio_caducidad")
    private String anioCaducidad;

    private String CVC;
}
