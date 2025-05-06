package org.grupob.adminapp.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO extends ProductoDTO{
    private UUID id;

    @NotBlank
    private String autor;

    @NotBlank
    private String editorial;

    @Min(1)
    private Integer numPaginas;

    public LibroDTO(String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria, String autor, String editorial, Integer numPaginas) {
        super(nombre, precio, descripcion, categoria);
        this.autor = autor;
        this.editorial = editorial;
        this.numPaginas = numPaginas;
    }
}
