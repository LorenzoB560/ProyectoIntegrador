package org.grupob.adminapp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicoDTO extends ProductoDTO{
    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotBlank
    private String garantia;
}
