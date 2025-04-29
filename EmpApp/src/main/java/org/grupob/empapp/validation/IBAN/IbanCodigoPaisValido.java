package org.grupob.empapp.validation.IBAN;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grupob.empapp.validation.IBAN.IbanCodigoPaisValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IbanCodigoPaisValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IbanCodigoPaisValido {

    String message() default "{IbanCodigoPaisValido.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
