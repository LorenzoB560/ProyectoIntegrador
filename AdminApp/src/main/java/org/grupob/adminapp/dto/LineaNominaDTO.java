package org.grupob.adminapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaNominaDTO {

    private Long idConcepto;
    private BigDecimal cantidad;
}
