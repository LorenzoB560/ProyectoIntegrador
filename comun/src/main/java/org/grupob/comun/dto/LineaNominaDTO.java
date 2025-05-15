package org.grupob.comun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaNominaDTO {

    private UUID idConcepto;
    private String nombreConcepto;
    private String tipoConcepto;
    private BigDecimal cantidad;
    private BigDecimal porcentaje;

}
