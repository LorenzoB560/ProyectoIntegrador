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

    private String mes;
    private String anio;
    private BigDecimal totalLiquido;

    private List<LineaNominaDTO> lineaNominas = new ArrayList<>();

    private UUID idEmpleado;
}
