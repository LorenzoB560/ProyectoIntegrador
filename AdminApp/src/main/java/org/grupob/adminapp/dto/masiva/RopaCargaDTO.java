package org.grupob.adminapp.dto.masiva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RopaCargaDTO extends ProductoCargaDTO {


    @NotNull(message = "Las tallas son obligatorias")
    @Size(min = 1, message = "Debe haber al menos una talla")
    private Set<@NotBlank(message = "La talla no puede estar vacía") String> tallas;
}
