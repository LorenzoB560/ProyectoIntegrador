package org.grupob.empapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupob.comun.dto.grupo_validaciones.GrupoDatosEconomicos;
import org.grupob.empapp.validation.IBAN.*;
import org.grupob.empapp.validation.entidad_bancaria.ExisteEntidadBancaria;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CodigoEntidadCorrecto(groups = GrupoDatosEconomicos.class)
@CodigoPaisEntidadCorrecto(groups = GrupoDatosEconomicos.class)
public class CuentaBancariaDTO {

    @NotNull(groups = GrupoDatosEconomicos.class)
    @ExisteEntidadBancaria(groups = GrupoDatosEconomicos.class)
    private UUID idEntidadBancaria;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(regexp = "[A-Z]{2}", message = "{codigoPais.message}", groups = GrupoDatosEconomicos.class)
    private String codigoPais;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(regexp = "\\d{2}", message = "{digitosControl.message}", groups = GrupoDatosEconomicos.class)
    private String digitosControl;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(regexp = "\\d{4}", message = "{codigoEntidadBancaria.message}", groups = GrupoDatosEconomicos.class)
    private String codigoEntidadBancaria;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(regexp = "\\d{4}", message = "{sucursal.message}", groups = GrupoDatosEconomicos.class)
    private String sucursal;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @NotBlank(groups = GrupoDatosEconomicos.class)
    @Pattern(regexp = "\\d{10}", message = "{numeroCuenta.message}", groups = GrupoDatosEconomicos.class)
    private String numeroCuenta;

    public String getIban() {
        if (codigoPais == null || digitosControl == null || codigoEntidadBancaria == null
                || sucursal == null || numeroCuenta == null) {
            return null;
        }
        return codigoPais + digitosControl + codigoEntidadBancaria + sucursal + numeroCuenta;
    }
}