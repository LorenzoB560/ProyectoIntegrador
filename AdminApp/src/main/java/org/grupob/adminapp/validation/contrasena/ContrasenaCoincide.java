package org.grupob.adminapp.validation.contrasena;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;



@Documented
@Constraint(validatedBy = ContrasenaCoincideValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ContrasenaCoincide {
    String message() default "Las contraseñas no coinciden";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
