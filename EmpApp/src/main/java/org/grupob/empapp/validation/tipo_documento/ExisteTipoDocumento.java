package org.grupob.empapp.validation.tipo_documento;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.tipo_via.ExisteTipoViaValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExisteTipoDocumentoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteTipoDocumento {
    String message() default "{Existe.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
