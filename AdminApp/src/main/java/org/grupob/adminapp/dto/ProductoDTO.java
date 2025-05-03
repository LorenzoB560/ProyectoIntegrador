package org.grupob.adminapp.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipoProducto")
@JsonSubTypes({
        @Type(value = LibroDTO.class, name = "LIBRO"),
        @Type(value = ElectronicoDTO.class, name = "ELECTRONICO"),
        @Type(value = RopaDTO.class, name = "ROPA")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    @NotBlank
    private String nombre;

    @DecimalMin("0.01")
    private BigDecimal precio;

    @NotBlank
    private String descripcion;

    @NotBlank
    private String categoriaNombre;
}
