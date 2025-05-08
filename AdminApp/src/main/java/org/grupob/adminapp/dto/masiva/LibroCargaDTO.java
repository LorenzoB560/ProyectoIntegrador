package org.grupob.adminapp.dto.masiva;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroCargaDTO extends ProductoCargaDTO {
    private UUID id;


    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotBlank(message = "La editorial es obligatoria")
    private String editorial;

    @NotNull(message = "El número de páginas es obligatorio")
    @Min(value = 1, message = "El número de páginas debe ser mayor que 0")
    private Integer numPaginas;


}
