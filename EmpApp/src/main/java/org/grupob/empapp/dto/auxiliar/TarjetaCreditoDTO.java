package org.grupob.empapp.dto.auxiliar;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaCreditoDTO {


    private String numeroTarjeta;
    private String mesCaducidad;
    private String anioCaducidad;
    private String CVC;
}
