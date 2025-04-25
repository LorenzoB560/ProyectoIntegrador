package org.grupob.empapp.validation.edad;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EdadNotBlankValidator implements ConstraintValidator<EdadNotBlank, String> {

    @Override
    public boolean isValid(String edad, ConstraintValidatorContext context) {
        return edad != null && !edad.trim().isEmpty();
    }
}
