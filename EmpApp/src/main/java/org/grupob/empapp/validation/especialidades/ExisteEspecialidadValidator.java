package org.grupob.empapp.validation.especialidades;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.repository.EspecialidadRepository;
import org.grupob.empapp.validation.departamento.ExisteDepartamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ExisteEspecialidadValidator implements ConstraintValidator<ExisteEspecialidad, Set<UUID>> {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public boolean isValid(Set<UUID> idsSeleccionados, ConstraintValidatorContext context) {
        try {
            List<UUID> existentes = especialidadRepository.findAllById(idsSeleccionados)
                    .stream()
                    .map(e -> e.getId())
                    .toList();

            return existentes.size() == idsSeleccionados.size();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
