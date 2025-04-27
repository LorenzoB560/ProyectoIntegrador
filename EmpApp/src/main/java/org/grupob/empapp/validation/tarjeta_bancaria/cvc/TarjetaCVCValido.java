//package org.grupob.empapp.validation.tarjeta_bancaria.cvc;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Constraint(validatedBy = TarjetaCVCCombinadoValidator.class)
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface TarjetaCVCValido {
//    String message() default "{TarjetaCVCValido.message}";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}