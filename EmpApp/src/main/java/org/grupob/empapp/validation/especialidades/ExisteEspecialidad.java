package org.grupob.empapp.validation.especialidades;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.departamento.ExisteDepartamentoValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExisteEspecialidadValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteEspecialidad {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
