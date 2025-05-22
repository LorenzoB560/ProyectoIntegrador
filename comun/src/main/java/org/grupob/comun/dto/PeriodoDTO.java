package org.grupob.comun.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupob.comun.dto.grupo_validaciones.GrupoDatosPersonales;
import org.grupob.comun.validation.fechas.LocalDateNotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoDTO {

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de finalización no puede ser nula")
    private LocalDate fechaFin;

    public static PeriodoDTO of(LocalDate fechaInicio, LocalDate fechaFin) {
        return new PeriodoDTO(fechaInicio, fechaFin);
    }

    public static PeriodoDTO from(LocalDate fechaInicio) {
        return new PeriodoDTO(fechaInicio, LocalDate.now());
    }
}
