package org.grupob.empapp.validation.tipo_tarjeta;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.comun.repository.TipoTarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExisteTipoTarjetaValidator implements ConstraintValidator<ExisteTipoTarjeta, Long> {

    @Autowired
    private TipoTarjetaRepository tipoTarjetaRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return tipoTarjetaRepository.existsTipoTarjetaCreditoById(id);
    }
}
