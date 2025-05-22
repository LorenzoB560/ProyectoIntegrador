package org.grupob.adminapp.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.dto.LineaNominaDTO;
import org.grupob.comun.dto.PeriodoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaNominaDTO {

    private UUID id;
    private UUID idEmpleado;

    @Valid
    private PeriodoDTO periodo;
    private BigDecimal totalLiquido;
    private List<LineaNominaDTO> lineaNominas;
}
