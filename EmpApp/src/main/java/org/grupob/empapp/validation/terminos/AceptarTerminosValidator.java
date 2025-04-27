package org.grupob.empapp.validation.terminos;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AceptarTerminosValidator implements ConstraintValidator<AceptarTerminos, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return "on".equals(value);
    }
}
