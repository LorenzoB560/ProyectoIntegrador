package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@AllArgsConstructor@NoArgsConstructor
@Data
public class DepartamentoDTO {

    private UUID id;
    private String codigo;
    private String nombre;
    private String localidad;

    public DepartamentoDTO(String codigo, String nombre,String localidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.localidad = localidad;
    }
}
