package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AltaEmpleadoDTO {

    // ** PASO 1 - DATOS PERSONALES **
    public interface GrupoPersonal {};

    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;


}
