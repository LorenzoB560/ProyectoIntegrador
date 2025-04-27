package org.grupob.adminapp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmpleadoSearchDTO {
    private String nombre;
    private String departamento;
    private String comentario;
    private LocalDate contratadosAntesDe;
    private BigDecimal salarioMinimo;
}