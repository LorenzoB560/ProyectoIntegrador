package org.grupob.empapp.validation.IBAN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.grupob.comun.repository.EntidadBancariaRepository;
import org.grupob.comun.repository.PaisRepository;
import org.grupob.empapp.dto.CuentaBancariaDTO;

@RequiredArgsConstructor
public class CodigoPaisEntidadCorrectoValidator implements ConstraintValidator<CodigoPaisEntidadCorrecto, CuentaBancariaDTO> {

    private final PaisRepository paisRepository;
    private final EntidadBancariaRepository entidadBancariaRepository;

    @Override
    public boolean isValid(CuentaBancariaDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getIdEntidadBancaria() == null || dto.getCodigoPais() == null) {
            return true;
        }

        return entidadBancariaRepository.findById(dto.getIdEntidadBancaria())
                .filter(entidad -> entidad.getPais() != null)
                .map(entidad -> entidad.getPais().getCodigo().equalsIgnoreCase(dto.getCodigoPais()))
                .orElse(false);
    }
}
