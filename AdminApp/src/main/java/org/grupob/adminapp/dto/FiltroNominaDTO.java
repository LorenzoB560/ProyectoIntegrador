package org.grupob.adminapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.maestras.Concepto;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroNominaDTO {

    private String empleado;
    private Integer mes;
    private Integer anio;
    private List<Concepto> conceptos;
    private BigDecimal totalLiquidoMinimo;
    private BigDecimal totalLiquidoMaximo;
}
