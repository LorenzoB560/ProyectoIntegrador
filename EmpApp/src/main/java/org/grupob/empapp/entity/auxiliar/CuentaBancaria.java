package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.*;
import lombok.*;
import org.grupob.empapp.entity.Empleado;

import java.util.Set;

@Getter
@Setter
@ToString
@Embeddable
public class CuentaBancaria {

    private String IBAN;

    protected CuentaBancaria() {
    }

    private CuentaBancaria(String IBAN) {
        this.IBAN = IBAN;
    }

    public static CuentaBancaria of(String IBAN) {
        return new CuentaBancaria(IBAN);
    }


}
