package org.grupob.comun.entity.auxiliar;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Telefono {
    private String prefijoTelefonico;
    private String numero;
}
