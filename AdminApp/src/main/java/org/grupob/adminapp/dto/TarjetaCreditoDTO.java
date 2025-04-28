package org.grupob.adminapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarjetaCreditoDTO {
    private String numero;
    private String mesCaducidad;
    private String anioCaducidad;
    private String cvc;
}
