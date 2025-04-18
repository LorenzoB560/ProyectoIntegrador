package org.grupob.empapp.dto.auxiliar;

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
    private String piso;
    private String puerta;
    private String codigoPostal;
    private String localidad;
    private String region;
    private String pais;
}
