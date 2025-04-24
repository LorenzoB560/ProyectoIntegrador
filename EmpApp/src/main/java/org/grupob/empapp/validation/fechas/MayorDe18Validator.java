package org.grupob.empapp.validation.fechas;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MayorDe18Validator implements ConstraintValidator<MayorDe18, LocalDate> {

    @Override
    public void initialize(MayorDe18 constraintAnnotation) {
        // No se necesita inicialización adicional
    }

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return false;
        }

        LocalDate fechaActual = LocalDate.now();

        Period periodo = Period.between(fechaNacimiento, fechaActual);

        return periodo.getYears() >= 18;
    }
}
