package org.grupob.empapp.validation.edad;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EdadNotBlankValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadNotBlank {
    String message() default "{EdadNotNull.mensaje}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


