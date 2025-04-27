package org.grupob.empapp.validation.genero;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.departamento.ExisteDepartamentoValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExisteGeneroValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteGenero {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
