package org.grupob.empapp.validation.documento_valido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentoValidoValidator.class)
@Target({ ElementType.TYPE }) // Aplica a toda la clase DTO
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentoValido {
    String message() default "{DocumentoValido.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
