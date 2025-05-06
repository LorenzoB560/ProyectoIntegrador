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
import java.util.UUID;

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

    private UUID id;

    @NotBlank
    private String nombre;

    @DecimalMin("0.01")
    private BigDecimal precio;

    @NotBlank
    private String descripcion;

    @NotBlank
    private CategoriaDTO categoria;

    public ProductoDTO(String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }
}
