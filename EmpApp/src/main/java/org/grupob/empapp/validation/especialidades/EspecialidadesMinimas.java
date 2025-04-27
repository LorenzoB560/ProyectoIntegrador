package org.grupob.empapp.validation.especialidades;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EspecialidadesMinimasValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EspecialidadesMinimas {
    String message() default "{EspecialidadesMinimas.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
