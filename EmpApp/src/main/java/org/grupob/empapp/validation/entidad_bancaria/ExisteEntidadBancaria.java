package org.grupob.empapp.validation.entidad_bancaria;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.departamento.ExisteDepartamentoValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExisteEntidadBancariaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteEntidadBancaria {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
