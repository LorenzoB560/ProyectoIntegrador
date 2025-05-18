package org.grupob.adminapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true) // Incluye campos de ProductoDTO
@ToString(callSuper = true)      // Incluye campos de ProductoDTO
@NoArgsConstructor
    public class RopaDTO extends ProductoDTO { // Extiende el DTO base actualizado

        // Hereda: id, nombre, precio, descripcion, categoria, etc. de ProductoDTO

        // Campos específicos de Ropa
        @NotEmpty(message = "Debe especificar al menos una talla") // Asegura que la lista no esté vacía
        private List<TallaDTO> tallas = new ArrayList<>(); // Lista de DTOs de Talla

        @NotBlank(message = "El material no puede estar vacío")
        private String material;


        // Constructor completo opcional
        public RopaDTO(UUID id, String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria,
                       List<TallaDTO> tallas, String material) {
            super(id, nombre, precio, descripcion, categoria); // Llama al constructor padre
            this.tallas = tallas != null ? tallas : new ArrayList<>();
            this.material = material;

        }
    }

