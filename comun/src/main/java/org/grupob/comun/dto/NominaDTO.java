package org.grupob.comun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NominaDTO {

    private UUID id;
    private UUID idEmpleado;
    private String nombre;
    private PeriodoDTO periodo;
    private BigDecimal totalLiquido;
    private List<LineaNominaDTO> lineaNominas;
}
