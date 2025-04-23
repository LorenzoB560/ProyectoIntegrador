package org.grupob.empapp.validation.contrasena;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Documented
@Constraint(validatedBy = ClaveCoincideValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClaveCoincide {
    String message() default "Las contraseñas no coinciden";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
