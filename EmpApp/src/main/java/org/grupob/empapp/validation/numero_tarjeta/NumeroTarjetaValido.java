package org.grupob.empapp.validation.numero_tarjeta;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumeroTarjetaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumeroTarjetaValido {
    String message() default "{NumeroTarjetaValido.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}