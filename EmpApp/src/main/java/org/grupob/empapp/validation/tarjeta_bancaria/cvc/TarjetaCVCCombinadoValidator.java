//package org.grupob.empapp.validation.tarjeta_bancaria.cvc;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import org.grupob.empapp.dto.TarjetaCreditoDTO;
//
//public class TarjetaCVCCombinadoValidator implements ConstraintValidator<TarjetaCVCValido, TarjetaCreditoDTO> {
//
//    @Override
//    public void initialize(TarjetaCVCValido constraintAnnotation) {
//        // No necesitamos inicialización especial
//    }
//
//    @Override
//    public boolean isValid(TarjetaCreditoDTO tarjeta, ConstraintValidatorContext context) {
//        // Si alguno de los campos es nulo, dejamos que @NotNull se encargue
//        if (tarjeta == null || tarjeta.getCvc() == null || tarjeta  .getIdTipoTarjeta() == null) {
//            return true;
//        }
//
//        String cvc = tarjeta.getCvc();
//        Long idTipoTarjeta = tarjeta.getIdTipoTarjeta();
//
//        // Verificar que el CVC solo contiene dígitos
//        if (!cvc.matches("\\d+")) {
//            addCustomConstraintViolation(context, "El CVC debe contener solo dígitos", "cvc");
//            return false;
//        }
//
//        boolean isValid;
//
//        // Validar según el tipo de tarjeta
//        if (idTipoTarjeta == 3) { // American Express
//            isValid = cvc.length() == 4;
//            if (!isValid) {
//                addCustomConstraintViolation(context, "El CVC para American Express debe tener 4 dígitos", "cvc");
//            }
//        } else { // Visa, Mastercard, etc.
//            isValid = cvc.length() == 3;
//            if (!isValid) {
//                addCustomConstraintViolation(context, "El CVC debe tener 3 dígitos", "cvc");
//            }
//        }
//
//        return isValid;
//    }
//
//    private void addCustomConstraintViolation(ConstraintValidatorContext context, String message, String property) {
//        context.disableDefaultConstraintViolation();
//        context.buildConstraintViolationWithTemplate(message)
//                .addPropertyNode(property)
//                .addConstraintViolation();
//    }
//}