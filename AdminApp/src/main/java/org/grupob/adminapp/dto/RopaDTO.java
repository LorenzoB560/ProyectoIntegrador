package org.grupob.adminapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RopaDTO extends ProductoDTO{
    @NotBlank
    private String talla;

    @NotBlank
    private String color;

    @NotBlank
    private String material;
}
