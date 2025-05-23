package org.grupob.comun.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionPostalDTO {
    private String tipoVia;
    private String via;
    private String numero;
}
