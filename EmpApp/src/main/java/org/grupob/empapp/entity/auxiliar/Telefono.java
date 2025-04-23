package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Embeddable
public class Telefono {
    private String prefijoTelefonico;
    private String numero;
}
