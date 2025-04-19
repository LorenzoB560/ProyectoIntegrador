package org.grupob.empapp.validation.fechas;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class LocalDateNotBlankValidator implements ConstraintValidator<LocalDateNotBlank, LocalDate> {

    @Override
    public void initialize(LocalDateNotBlank constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate fecha, ConstraintValidatorContext constraintValidatorContext) {
        return fecha != null && !fecha.equals(LocalDate.MIN);
    }


}
