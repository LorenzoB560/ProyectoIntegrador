package org.grupob.comun.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

@Data
public class EmpleadoSearchDTO {
    private String nombre;
    private String departamento;
    private String comentario;



    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Past(message = "{registro.fechaNacimiento.past}" )
    private LocalDate contratadosAntesDe;

    private BigDecimal salarioMaximo;
    private BigDecimal salarioMinimo;
}