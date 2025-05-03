package org.grupob.adminapp.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO extends ProductoDTO{
    @NotBlank
    private String autor;

    @NotBlank
    private String editorial;

    @Min(1)
    private Integer numPaginas;
}
