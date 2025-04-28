package org.grupob.empapp.validation.IBAN;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CodigoPaisEntidadCorrectoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CodigoPaisEntidadCorrecto {
    String message() default "{CodigoPaisEntidadCorrecto.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
