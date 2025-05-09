package org.grupob.adminapp.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Incluye campos de ProductoDTO
@ToString(callSuper = true)      // Incluye campos de ProductoDTO
@NoArgsConstructor
public class LibroDTO extends ProductoDTO{


    @NotBlank
    private String autor;

    @NotBlank
    private String editorial;

    @Min(1)
    private Integer numPaginas;

    public LibroDTO(UUID id, String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria, String autor, String editorial, Integer numPaginas) {
        super(id ,nombre, precio, descripcion, categoria);
        this.autor = autor;
        this.editorial = editorial;
        this.numPaginas = numPaginas;
    }
}
