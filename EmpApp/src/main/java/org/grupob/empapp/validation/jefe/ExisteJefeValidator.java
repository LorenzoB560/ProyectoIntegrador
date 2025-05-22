package org.grupob.empapp.validation.jefe;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.empapp.validation.departamento.ExisteDepartamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExisteJefeValidator implements ConstraintValidator<ExisteJefe, UUID> {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        // Add a null check here
        if (id == null) {
            // If the ID is null, this validator considers it valid.
            // Use @NotNull on the field if it's mandatory.
            return true;
        }
        // If the ID is not null, then proceed to check its existence.
        return empleadoRepository.existsById(id);
    }
}
