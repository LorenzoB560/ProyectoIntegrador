package org.grupob.empapp.dto.auxiliar;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosContacto;
import org.grupob.empapp.validation.tipo_documento.ExisteTipoDocumento;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoDTO {

    private Long id;

    @NotNull(groups = GrupoDatosContacto.class)
    @ExisteTipoDocumento(groups = GrupoDatosContacto.class)
    private String documento;
}
