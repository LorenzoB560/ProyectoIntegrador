package org.grupob.comun.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroNominaDTO {

    private String filtroNombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<String> conceptosSeleccionados;
}
