package org.grupob.empapp.validation.tarjeta_bancaria.tipo_tarjeta;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExisteTipoTarjetaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteTipoTarjeta {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
