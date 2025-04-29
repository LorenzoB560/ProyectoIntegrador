package org.grupob.empapp.validation.especialidades;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class EspecialidadesMinimasValidator implements ConstraintValidator<EspecialidadesMinimas, Set<Especialidad>> {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public boolean isValid(Set<Especialidad> especialidadesSeleccionadas, ConstraintValidatorContext context) {
//        if (especialidadesSeleccionadas == null || especialidadesSeleccionadas.isEmpty()) {
//            return true;
//        }
        return especialidadesSeleccionadas.size() >= 2;
    }

}
