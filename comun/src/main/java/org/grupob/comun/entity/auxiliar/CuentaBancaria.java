package org.grupob.comun.entity.auxiliar;

import jakarta.persistence.*;
import lombok.*;
import org.grupob.comun.entity.EntidadBancaria;

@Getter
@Setter
@ToString
@Embeddable
public class CuentaBancaria {

//    @ManyToOne
//    @JoinColumn(name = "id_entidad_bancaria", foreignKey = @ForeignKey(name = "FK_empleado_entidad_bancaria_id"))
//    private EntidadBancaria entidadBancaria;
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
