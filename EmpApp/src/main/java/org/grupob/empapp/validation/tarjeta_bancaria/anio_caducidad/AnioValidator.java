package org.grupob.empapp.validation.tarjeta_bancaria.anio_caducidad;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class AnioValidator implements ConstraintValidator<AnioValido, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !value.matches("\\d{4}")) {
            return false;
        }
        int anio = Integer.parseInt(value);
        int anioActual = Year.now().getValue();
        return anio >= anioActual && anio <= (anioActual + 20);
    }
}
