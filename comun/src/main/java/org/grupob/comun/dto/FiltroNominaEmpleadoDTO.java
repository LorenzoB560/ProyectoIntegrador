package org.grupob.comun.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroNominaEmpleadoDTO {

    private UUID idEmpleado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
