package org.grupob.empapp.validation.edad;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EdadFechaNacimientoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadCoincideConFechaNacimiento {
    String message() default "{EdadCoincideConFechaNacimiento.mensaje}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
