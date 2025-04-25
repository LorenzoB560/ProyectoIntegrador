package org.grupob.empapp.validation.pais;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistePaisValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistePais {
    String message() default "{ExistePais.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
