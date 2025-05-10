package org.grupob.comun.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroNominaEmpleadoDTO {

    private UUID idEmpleado;
    private Integer filtroMes;
    private Integer filtroAnio;
    private List<String> conceptosSeleccionados;
    private BigDecimal totalLiquidoMinimo;
    private BigDecimal totalLiquidoMaximo;
}
