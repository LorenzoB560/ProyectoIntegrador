package org.grupob.adminapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.grupob.adminapp.dto.DimensionDTO; // Importa DimensionDTO

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true) // Incluye campos de ProductoDTO
@ToString(callSuper = true)      // Incluye campos de ProductoDTO
@NoArgsConstructor
public class MuebleDTO extends ProductoDTO { // Extiende el DTO base (asegúrate que ProductoDTO esté actualizado)

    // Hereda: id, nombre, precio, descripcion, categoria, proveedor, etc. de ProductoDTO

    // Campos específicos de Mueble
    @Valid // Para validar el DTO anidado
    @NotNull(message = "Las dimensiones son requeridas")
    private DimensionDTO dimension; // Usa el DTO de Dimension que creamos

    @NotBlank(message = "El material no puede estar vacío")
    private String material;

    @NotEmpty(message = "Debe especificar al menos un color") // O @NotNull si la lista puede estar pero no ser null
    private List<String> colores = new ArrayList<>(); // Lista de strings para colores

    // Constructor completo opcional
    public MuebleDTO(UUID id, String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria,
                     ProveedorDTO proveedor, /* otros campos comunes */
                     DimensionDTO dimension, String material, List<String> colores) {
        super(id, nombre, precio, descripcion, categoria /*, proveedor, otros */ ); // Llama al constructor padre adecuado
        this.dimension = dimension;
        this.material = material;
        this.colores = colores != null ? colores : new ArrayList<>();
    }
}