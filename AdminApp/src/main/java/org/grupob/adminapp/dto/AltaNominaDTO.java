package org.grupob.adminapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaNominaDTO {

    private UUID id;
    private UUID idEmpleado;
    private int mes;
    private int anio;
    private BigDecimal totalLiquido;
    private List<LineaNominaDTO> lineaNominas;
}
