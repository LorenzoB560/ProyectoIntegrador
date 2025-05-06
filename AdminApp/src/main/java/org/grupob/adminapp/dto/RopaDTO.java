package org.grupob.adminapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RopaDTO extends ProductoDTO{
    private UUID id;

    @NotBlank
    private String talla;

    @NotBlank
    private String color;

    @NotBlank
    private String material;

    public RopaDTO(String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria, String talla, String color, String material) {
        super(nombre, precio, descripcion, categoria);
        this.talla = talla;
        this.color = color;
        this.material = material;
    }
}
