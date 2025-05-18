package org.grupob.empapp.dto; // O simplemente org.grupob.adminapp.dto

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               // Genera Getters, Setters, toString, equals, hashCode
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class TallaDTO {

    // ID de la entidad Talla (autoincremental)
    private Long id;

    // Nombre de la talla (ej: "S", "M", "42")
    // Usamos la anotación de validación @NotBlank para asegurar que no sea nulo ni vacío
    @NotBlank(message = "El nombre de la talla no puede estar vacío")
    private String talla; // Coincide con el nombre del campo en la entidad

}