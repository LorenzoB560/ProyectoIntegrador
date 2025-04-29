package org.grupob.empapp.validation.departamento;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExisteDepartamentoValidator implements ConstraintValidator<ExisteDepartamento, UUID> {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        return departamentoRepository.existsDepartamentoById(id);
    }
}
