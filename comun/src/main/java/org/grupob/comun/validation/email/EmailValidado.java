package org.grupob.comun.validation.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidadoValidator.class)
@Documented
public @interface EmailValidado {

    String message() default "{EmailValidado.mensaje}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
