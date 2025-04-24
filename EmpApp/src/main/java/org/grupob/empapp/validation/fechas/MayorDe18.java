package org.grupob.empapp.validation.fechas;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = MayorDe18Validator.class)
@Documented
public @interface MayorDe18 {

    String message() default "{MayorDe18.mensaje}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
