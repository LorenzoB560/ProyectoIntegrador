package org.grupob.empapp.validation.genero;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.grupob.empapp.validation.departamento.ExisteDepartamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExisteGeneroValidator implements ConstraintValidator<ExisteGenero, Long> {

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return generoRepository.existsGeneroById(id);
    }
}
