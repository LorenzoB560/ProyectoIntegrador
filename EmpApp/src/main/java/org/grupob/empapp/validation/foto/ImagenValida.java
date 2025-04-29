package org.grupob.empapp.validation.foto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImagenValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImagenValida {
    String message() default "{ValidarImagen.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int maxSizeKB() default 200; // Puedes configurar el límite de tamaño si quieres
}
