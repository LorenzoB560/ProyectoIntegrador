package org.grupob.empapp.validation.entidad_bancaria;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.comun.repository.EntidadBancariaRepository;
import org.grupob.empapp.validation.departamento.ExisteDepartamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExisteEntidadBancariaValidator implements ConstraintValidator<ExisteEntidadBancaria, UUID> {

    @Autowired
    private EntidadBancariaRepository entidadBancariaRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        return entidadBancariaRepository.existsEntidadBancariaById(id);
    }
}
