package org.grupob.comun.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public static PeriodoDTO of(LocalDate fechaInicio, LocalDate fechaFin) {
        return new PeriodoDTO(fechaInicio, fechaFin);
    }

    public static PeriodoDTO from(LocalDate fechaInicio) {
        return new PeriodoDTO(fechaInicio, LocalDate.now());
    }
}
