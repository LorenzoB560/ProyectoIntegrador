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
public class ElectronicoDTO extends ProductoDTO{
    private UUID id;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotBlank
    private String garantia;

    public ElectronicoDTO(String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria, String marca, String modelo, String garantia) {
        super(nombre, precio, descripcion, categoria);
        this.marca = marca;
        this.modelo = modelo;
        this.garantia = garantia;
    }
}
