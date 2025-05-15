package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaBancariaEmpleadoDTO {

    private String iban;

    public static CuentaBancariaEmpleadoDTO of(String iban) {
        return new CuentaBancariaEmpleadoDTO(iban);
    }
}
