package org.grupob.adminapp.dto.masiva;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({ //Recomendable ponerlo de cara a seguridad pero no es obligatorio
        @JsonSubTypes.Type(LibroCargaDTO.class),
        @JsonSubTypes.Type(ElectronicoCargaDTO.class),
        @JsonSubTypes.Type(RopaCargaDTO.class),
        @JsonSubTypes.Type(MuebleCargaDTO.class)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCargaDTO {

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", inclusive = false, message = "El precio debe ser mayor que 0")
    private BigDecimal precio;

    private String marca;

    @NotNull(message = "Las categorías son obligatorias")
    @Size(min = 1, message = "Debe haber al menos una categoría")
    private LinkedHashSet<@NotBlank(message = "El nombre de la categoría no puede estar vacío") String> categorias;

    private Boolean segundaMano;

    @NotNull(message = "Las unidades son obligatorias")
    @Min(value = 1, message = "Las unidades deben ser mayores que 0")
    private Integer unidades;

    @NotNull(message = "La fecha de fabricación es obligatoria")
    @Past(message = "La fecha de fabricación debe ser pasada")
    private LocalDate fechaFabricacion;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String proveedor;


}
