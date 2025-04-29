package org.grupob.empapp.validation.IBAN;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.IBAN.CodigoEntidadCorrectoValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CodigoEntidadCorrectoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CodigoEntidadCorrecto {
    String message() default "{CodigoEntidadCorrecto.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
