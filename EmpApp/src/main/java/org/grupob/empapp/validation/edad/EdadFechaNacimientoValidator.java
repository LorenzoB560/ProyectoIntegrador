package org.grupob.empapp.validation.edad;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.empapp.dto.AltaEmpleadoDTO;

import java.time.LocalDate;
import java.time.Period;

public class EdadFechaNacimientoValidator implements ConstraintValidator<EdadCoincideConFechaNacimiento, AltaEmpleadoDTO> {

    @Override
    public boolean isValid(AltaEmpleadoDTO dto, ConstraintValidatorContext context) {
        if (dto.getFechaNacimiento() == null || dto.getEdad() == null) {
            return true; // Lo validan otras anotaciones como @NotNull si hace falta
        }

        int edadCalculada = Period.between(dto.getFechaNacimiento(), LocalDate.now()).getYears();

        // El problema podría estar aquí - comprobamos que la edad sea un Integer
        // y lo comparamos directamente con el valor calculado
        try {
            int edadIngresada = Integer.parseInt(dto.getEdad());
            return edadIngresada == edadCalculada;
        } catch (NumberFormatException e) {
            return false; // Si la edad no es un número válido, la validación falla
        }
    }
}