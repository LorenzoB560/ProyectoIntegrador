package org.grupob.empapp.validation.documento_valido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.empapp.dto.AltaEmpleadoDTO;

public class DocumentoValidoValidator implements ConstraintValidator<DocumentoValido, AltaEmpleadoDTO> {

    @Override
    public boolean isValid(AltaEmpleadoDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        String tipo = dto.getTipoDocumento();
        String numero = dto.getNumDocumento();

        if (tipo == null || numero == null) return true;

        switch (tipo.trim().toUpperCase()) {
            case "DNI":
                return numero.matches("^[0-9]{8}[A-Z]$");
            case "NIE":
                return numero.matches("^[XYZ][0-9]{7}[A-Z]$");
            case "PASAPORTE":
                return numero.matches("^[A-Z0-9]{5,15}$");
            default:
                // Si el tipo de documento no es conocido, que falle la validación
                return false;
        }
    }
}
