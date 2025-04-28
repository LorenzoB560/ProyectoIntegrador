package org.grupob.empapp.validation.tarjeta_bancaria.cvc;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.empapp.dto.AltaEmpleadoDTO;

public class CVCValidator implements ConstraintValidator<CVCValido, AltaEmpleadoDTO> {

    @Override
    public boolean isValid(AltaEmpleadoDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getTarjetaCredito() == null) {
            return true; // Si no hay datos, no validar (deja que @NotNull lo maneje)
        }

        Long idTipoTarjeta = dto.getIdTipoTarjeta();
        String cvc = dto.getTarjetaCredito().getCvc(); // Asumiendo que tienes getCvc()

        if (idTipoTarjeta == null || cvc == null) {
            return true; // Otro validador debe chequear nulls
        }

        // Verificar que solo tenga dígitos
        if (!cvc.matches("\\d+")) {
            return false;
        }

        int length = cvc.length();

        // Aquí validamos según el tipo de tarjeta (puedes ponerlo en un Enum o Map si prefieres)
        switch (idTipoTarjeta.intValue()) {
            case 1: // Visa Classic
            case 2: // Mastercard Gold
            case 4: // Visa Platinum
                return length == 3;
            case 3: // American Express
                return length == 4;
            default:
                return false; // Tipo de tarjeta desconocido
        }
    }
}
