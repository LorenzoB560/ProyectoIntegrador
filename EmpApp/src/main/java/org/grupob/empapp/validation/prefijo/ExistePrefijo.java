package org.grupob.empapp.validation.prefijo;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistePrefijoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistePrefijo {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
