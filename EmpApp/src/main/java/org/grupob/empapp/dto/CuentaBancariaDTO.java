package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaBancariaDTO {

    private String iban;

    public static CuentaBancariaDTO of(String iban) {
        return new CuentaBancariaDTO(iban);
    }
}