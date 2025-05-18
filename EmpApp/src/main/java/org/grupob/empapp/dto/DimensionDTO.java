package org.grupob.empapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDTO {

    // Se asume que las dimensiones no pueden ser nulas ni negativas
    @NotNull(message = "El ancho no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El ancho debe ser positivo") // No permite 0
    private BigDecimal ancho;

    @NotNull(message = "La profundidad no puede ser nula")
    @DecimalMin(value = "0.0", inclusive = false, message = "La profundidad debe ser positiva")
    private BigDecimal profundo;

    @NotNull(message = "El alto no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El alto debe ser positivo")
    private BigDecimal alto;
}
