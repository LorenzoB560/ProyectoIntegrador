package org.grupob.empapp.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.grupob.empapp.dto.DimensionDTO;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true) // Incluye campos de ProductoDTO
@ToString(callSuper = true)      // Incluye campos de ProductoDTO
@NoArgsConstructor
public class ElectronicoDTO extends ProductoDTO { // Asegúrate que ProductoDTO esté actualizado

    // Hereda: id, nombre, precio, descripcion, categoria de ProductoDTO

    private String modelo; // Asumiendo que puede ser opcional

    private String garantia; // Asumiendo que puede ser opcional

    @PositiveOrZero(message = "Las pulgadas no pueden ser negativas") // Permite 0 si aplica
    private BigDecimal pulgadas; // Mantiene BigDecimal

    @Valid // Indica a Spring que valide también el objeto DimensionDTO anidado
    @NotNull(message = "Las dimensiones son requeridas") // El objeto Dimension no puede ser nulo
    private DimensionDTO dimension; // Usa el DTO que creamos

    @PositiveOrZero(message = "La capacidad de la batería no puede ser negativa")
    private Integer capacidadBateria;

    @PositiveOrZero(message = "El almacenamiento interno no puede ser negativo")
    private Integer almacenamientoInterno;

    @PositiveOrZero(message = "La RAM no puede ser negativa")
    private Integer ram;

    // Constructor completo opcional (incluye campos heredados y DimensionDTO)
    public ElectronicoDTO(UUID id, String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria,
                          String modelo, String garantia, BigDecimal pulgadas, DimensionDTO dimension,
                          Integer capacidadBateria, Integer almacenamientoInterno, Integer ram) {
        // Llama al constructor de ProductoDTO para inicializar campos comunes
        super(id, nombre, precio, descripcion, categoria);
        // Inicializa campos específicos de ElectronicoDTO
        this.modelo = modelo;
        this.garantia = garantia;
        this.pulgadas = pulgadas;
        this.dimension = dimension;
        this.capacidadBateria = capacidadBateria;
        this.almacenamientoInterno = almacenamientoInterno;
        this.ram = ram;
    }
}
