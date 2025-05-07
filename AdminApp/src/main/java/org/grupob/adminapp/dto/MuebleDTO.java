package org.grupob.adminapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.grupob.adminapp.dto.auxiliar.DimensionDTO;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class MuebleDTO extends ProductoDTO {

    @NotBlank(message = "El material es obligatorio")
    private String material;

    @NotNull(message = "Las dimensiones son obligatorias")
    private DimensionDTO dimension;

    @NotNull(message = "Los colores son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un color")
    private Set<@NotBlank(message = "El color no puede estar vacío") String> colores;
}
