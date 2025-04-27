package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosEconomicos;
import org.grupob.empapp.validation.numero_tarjeta.NumeroTarjetaValido;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarjetaCreditoDTO {

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @NumeroTarjetaValido(groups = GrupoDatosEconomicos.class)
    private String numero;


    private String mesCaducidad;
    private String anioCaducidad;
    private String cvc;
}
