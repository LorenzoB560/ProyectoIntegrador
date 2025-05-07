package org.grupob.adminapp.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.grupob.adminapp.dto.auxiliar.DimensionDTO;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicoDTO extends ProductoDTO {
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotBlank(message = "La garantía es obligatoria")
    private String garantia;

    @DecimalMin(value = "0.1", inclusive = false, message = "Las pulgadas deben ser mayores que 0")
    private BigDecimal pulgadas;

    @NotNull(message = "Las dimensiones son obligatorias")
    private DimensionDTO dimension;

    @Min(value = 1, message = "La capacidad de batería debe ser mayor que 0")
    private Integer capacidadBateria;

    @Min(value = 1, message = "El almacenamiento interno debe ser mayor que 0")
    private Integer almacenamientoInterno;

    @Min(value = 1, message = "La RAM debe ser mayor que 0")
    private Integer ram;
}
