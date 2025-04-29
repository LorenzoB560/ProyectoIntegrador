package org.grupob.empapp.validation.pais;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistePaisValidator implements ConstraintValidator<ExistePais, String> {

    @Autowired
    private PaisRepository paisRepository;

    @Override
    public boolean isValid(String pais, ConstraintValidatorContext context) {
        return paisRepository.existsPaisByPais(pais);
    }
}
