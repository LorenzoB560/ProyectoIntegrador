package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.dto.grupo_validaciones.GrupoDatosEconomicos;
import org.grupob.empapp.validation.tarjeta_bancaria.anio_caducidad.AnioValido;
import org.grupob.empapp.validation.tarjeta_bancaria.numero_tarjeta.NumeroTarjetaValido;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarjetaCreditoDTO {

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @NumeroTarjetaValido(groups = GrupoDatosEconomicos.class)
    private String numero;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(regexp = "^(0[1-9]|1[0-2])$",
            message = "{mesCaducidad.message}",
            groups = GrupoDatosEconomicos.class)
    private String mesCaducidad;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(
            regexp = "^(\\d{4})$",
            message = "{anioCaducidad.message}",
            groups = GrupoDatosEconomicos.class
    )
    @AnioValido(groups = GrupoDatosEconomicos.class)
    private String anioCaducidad;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    private String cvc;
}

