package org.grupob.empapp.validation.IBAN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.empapp.dto.CuentaBancariaDTO;
import java.math.BigInteger;

public class IbanCodigoPaisValidator implements ConstraintValidator<IbanCodigoPaisValido, CuentaBancariaDTO> {

    @Override
    public boolean isValid(CuentaBancariaDTO cuentaBancariaDTO, ConstraintValidatorContext context) {
        if (cuentaBancariaDTO == null || cuentaBancariaDTO.getCodigoPais() == null ||
                cuentaBancariaDTO.getDigitosControl() == null || cuentaBancariaDTO.getCodigoEntidadBancaria() == null ||
                cuentaBancariaDTO.getSucursal() == null || cuentaBancariaDTO.getNumeroCuenta() == null) {
            return true; // Deja que @NotNull se encargue de estos casos
        }

        try {
            // Construimos el IBAN completo
            String iban = cuentaBancariaDTO.getCodigoPais() +
                    cuentaBancariaDTO.getDigitosControl() +
                    cuentaBancariaDTO.getCodigoEntidadBancaria() +
                    cuentaBancariaDTO.getSucursal() +
                    cuentaBancariaDTO.getNumeroCuenta();

            // Validamos el IBAN utilizando el algoritmo ISO
            return isValidIBAN(iban);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean isValidIBAN(String iban) {
        // Verificamos longitud básica y formato
        if (iban == null || iban.length() < 5) {
            return false;
        }

        // Movemos los 4 primeros caracteres al final
        String rearranged = iban.substring(4) + iban.substring(0, 4);

        // Convertimos letras a números según ISO 13616
        StringBuilder numericIban = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                // A=10, B=11, ..., Z=35
                numericIban.append(Character.toUpperCase(c) - 'A' + 10);
            } else {
                numericIban.append(c);
            }
        }

        // Calculamos el módulo 97
        try {
            BigInteger numericValue = new BigInteger(numericIban.toString());
            return numericValue.mod(BigInteger.valueOf(97)).intValue() == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}