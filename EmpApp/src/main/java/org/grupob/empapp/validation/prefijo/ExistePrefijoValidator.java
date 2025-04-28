package org.grupob.empapp.validation.prefijo;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.PaisRepository;
import org.grupob.empapp.validation.prefijo.ExistePrefijo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistePrefijoValidator implements ConstraintValidator<ExistePrefijo, String> {

    @Autowired
    private PaisRepository paisRepository;

    @Override
    public boolean isValid(String prefijo, ConstraintValidatorContext context) {
        return prefijo != null && paisRepository.existsPaisByPrefijo(prefijo);
    }
}
