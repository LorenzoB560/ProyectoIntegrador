package org.grupob.adminapp.dto.auxiliar;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DimensionDTO {
    @DecimalMin(value = "0.1", inclusive = false, message = "El ancho deben ser mayores que 0")
    private BigDecimal ancho;
    @DecimalMin(value = "0.1", inclusive = false, message = "La profundidad deben ser mayores que 0")
    private BigDecimal profundo;
    @DecimalMin(value = "0.1", inclusive = false, message = "El alto deben ser mayores que 0")
    private BigDecimal alto;
}
