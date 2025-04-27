package org.grupob.empapp.validation.tarjeta_bancaria.cvc;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.tarjeta_bancaria.cvc.CVCValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CVCValidator.class)
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CVCValido {
    String message() default "{CVCValido.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Campo opcional para indicar el ID del tipo de tarjeta
    long tipoTarjetaId() default 0;
}