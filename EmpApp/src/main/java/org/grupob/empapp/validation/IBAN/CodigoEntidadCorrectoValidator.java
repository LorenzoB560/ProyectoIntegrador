package org.grupob.empapp.validation.IBAN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.grupob.comun.repository.EntidadBancariaRepository;
import org.grupob.empapp.dto.CuentaBancariaDTO;

@RequiredArgsConstructor
public class CodigoEntidadCorrectoValidator implements ConstraintValidator<CodigoEntidadCorrecto, CuentaBancariaDTO> {

    private final EntidadBancariaRepository entidadBancariaRepository;

    @Override
    public boolean isValid(CuentaBancariaDTO cuentaBancariaDTO, ConstraintValidatorContext context) {
        if (cuentaBancariaDTO == null || cuentaBancariaDTO.getIdEntidadBancaria() == null || cuentaBancariaDTO.getCodigoEntidadBancaria() == null) {
            return true; // Deja que @NotNull se encargue de estos casos
        }

        return entidadBancariaRepository.findById(cuentaBancariaDTO.getIdEntidadBancaria())
                .map(entidad -> entidad.getCodigo().equals(cuentaBancariaDTO.getCodigoEntidadBancaria()))
                .orElse(false);
    }
}
