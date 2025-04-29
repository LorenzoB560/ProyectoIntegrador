package org.grupob.empapp.validation.tarjeta_bancaria.anio_caducidad;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AnioValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnioValido {
    String message() default "{AnioValido.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
