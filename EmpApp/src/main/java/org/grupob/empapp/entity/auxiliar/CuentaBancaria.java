package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class CuentaBancaria {
    @Column(name = "numero_cuenta", nullable = false)
    private LocalDate numcuenta;

    @Column(name = "Entidad_Bancaria", nullable = false)
    private String entidadBancaria;


}
