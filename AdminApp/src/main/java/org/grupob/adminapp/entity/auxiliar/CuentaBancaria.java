package org.grupob.adminapp.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Embeddable
public class CuentaBancaria {
    @Column(name = "numero_cuenta")
    private String numCuenta;

    @Column(name = "entidad_bancaria")
    private String entidadBancaria;

    protected CuentaBancaria() {
    }

    private CuentaBancaria(String numCuenta, String entidadBancaria) {
        this.numCuenta = numCuenta;
        this.entidadBancaria = entidadBancaria;
    }

    public static CuentaBancaria of(String numCuenta, String entidadBancaria) {
        return new CuentaBancaria(numCuenta, entidadBancaria);
    }


}
