package org.grupob.comun.validation.fechas;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = LocalDateNotBlankValidator.class)
@Documented
public @interface LocalDateNotBlank {

    String message() default "{LocalDateNotBlank.mensaje}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
