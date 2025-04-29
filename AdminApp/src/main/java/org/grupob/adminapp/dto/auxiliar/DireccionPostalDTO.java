package org.grupob.adminapp.dto.auxiliar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.adminapp.dto.grupoValidaciones.GrupoDireccion;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionPostalDTO {

    @NotBlank(groups = GrupoDireccion.class)
    private String tipoVia;

    @NotBlank(groups = GrupoDireccion.class)
    private String via;

    @NotBlank(groups = GrupoDireccion.class)
    private String numero;

    @NotNull(groups = GrupoDireccion.class)
    private String piso;

    @NotNull(groups = GrupoDireccion.class)
    private String puerta;

    @NotBlank(groups = GrupoDireccion.class)
    private String codigoPostal;

    @NotBlank(groups = GrupoDireccion.class)
    private String localidad;

    @NotBlank(groups = GrupoDireccion.class)
    private String region;

    @NotBlank(groups = GrupoDireccion.class)
    private String pais;
}
