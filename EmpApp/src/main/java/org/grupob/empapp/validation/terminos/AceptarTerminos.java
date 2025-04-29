package org.grupob.empapp.validation.terminos;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AceptarTerminosValidator.class)  // Vincula con el validador
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AceptarTerminos {

    String message() default "{AceptarTerminos.message}";  // Mensaje por defecto

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
