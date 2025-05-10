package org.grupob.empapp.dto.auxiliar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.grupob.comun.dto.grupo_validaciones.GrupoDatosContacto;
import org.grupob.empapp.validation.tipo_via.ExisteTipoVia;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class DireccionPostalDTO {

    public DireccionPostalDTO(){
        this.tipoVia = "Calle";
    }

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    @ExisteTipoVia(groups = GrupoDatosContacto.class)
    private String tipoVia;

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    private String via;

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    private String numero;

    @NotNull(groups = GrupoDatosContacto.class)
    private String portal;

    @NotNull(groups = GrupoDatosContacto.class)
    @Positive(groups = GrupoDatosContacto.class)
    private Integer planta;

    @NotNull(groups = GrupoDatosContacto.class)
    private String puerta;

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    private String localidad;

    @NotNull(groups = GrupoDatosContacto.class)
    private String region;

    @NotNull
    @NotBlank(groups = GrupoDatosContacto.class)
    private String codigoPostal;

}
