package org.grupob.empapp.validation.tipo_via;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.pais.ExistePaisValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExisteTipoViaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteTipoVia {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
