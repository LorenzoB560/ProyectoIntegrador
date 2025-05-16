package org.grupob.comun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoNominaDTO {

    // Campos heredados de Persona (o los que quieras exponer)
    private UUID id;
    private String nombre;
    private String apellido;
    private String numDocumento;
    private DireccionPostalDTO direccion;
    private DepartamentoDTO departamento;


}
