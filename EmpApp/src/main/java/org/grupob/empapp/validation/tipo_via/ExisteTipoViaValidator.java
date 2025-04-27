package org.grupob.empapp.validation.tipo_via;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.PaisRepository;
import org.grupob.comun.repository.TipoViaRepository;
import org.grupob.empapp.validation.pais.ExistePais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExisteTipoViaValidator implements ConstraintValidator<ExisteTipoVia, String> {

    @Autowired
    private TipoViaRepository tipoViaRepository;

    @Override
    public boolean isValid(String via, ConstraintValidatorContext context) {
        return via != null && tipoViaRepository.existsByTipoVia(via);
    }
}
