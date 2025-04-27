package org.grupob.empapp.validation.especialidades;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExisteEspecialidadValidator implements ConstraintValidator<ExisteEspecialidad, Set<Especialidad>> {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public boolean isValid(Set<Especialidad> especialidadesSeleccionadas, ConstraintValidatorContext context) {
        try {
            // Obtén las especialidades existentes en la base de datos (compara por id o el atributo único de la entidad)
            List<Especialidad> existentes = especialidadRepository.findAllById(
                    especialidadesSeleccionadas.stream()
                            .map(Especialidad::getId) // Suponiendo que Especialidad tiene un id que es único
                            .collect(Collectors.toList())
            );

            // Verifica si el tamaño de las especialidades existentes es igual al tamaño de las seleccionadas
            return existentes.size() == especialidadesSeleccionadas.size();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
