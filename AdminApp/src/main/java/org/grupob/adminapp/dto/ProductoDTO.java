package org.grupob.adminapp.dto;

// Importaciones necesarias
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data               // Getters, Setters, toString, equals, hashCode
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Setter@Getter
public class ProductoDTO {

    private UUID id; // Coincide con la entidad

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion; // Coincide con la entidad

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser positivo")
    private BigDecimal precio; // Coincide con la entidad

    private String marca; // Coincide con la entidad (opcional)

    // Para la relación ManyToMany con Categoria
    // Usamos un Set para evitar duplicados y coincidir con la entidad
    @NotEmpty(message = "Debe asignarse al menos una categoría") // O @NotNull si puede estar vacío pero no nulo
    private Set<CategoriaDTO> categoria = new HashSet<>(); // Coincide con la entidad ('categorias' renombrado a 'categoria' en DTO?)
    // Asegúrate que CategoriaDTO exista

    private Boolean segundaMano; // Coincide con la entidad (opcional)

    @NotNull(message = "Las unidades no pueden ser nulas")
    @Min(value = 0, message = "Las unidades no pueden ser negativas") // Permite 0 unidades? Ajustar si no.
    private Integer unidades; // Coincide con la entidad

    // Fecha de fabricación es opcional en la entidad (sin @Column(nullable=false))
    private LocalDate fechaFabricacion; // Coincide con la entidad

    @NotNull(message = "El proveedor no puede ser nulo") // Asumiendo que es obligatorio como en PDF
    private ProveedorDTO proveedor; // Coincide con la entidad. Asegúrate que ProveedorDTO exista

    // Fecha de alta es opcional en la entidad (sin @Column(nullable=false)),
    // pero podría generarse automáticamente en el backend (@CreationTimestamp)
    // por lo que quizás no sea necesaria en el DTO de entrada, pero sí de salida.
    private LocalDate fechaAlta; // Coincide con la entidad

    @Min(value = 0, message = "La valoración no puede ser negativa")
    private Integer valoracion; // Coincide con la entidad (nullable, default 0 en PDF?)

    // --- CAMPOS DE SUBCLASES (Añadir aquí) ---
    // Añade aquí los campos específicos que vayan a tener los DTOs de subclases
    // si decides usar un DTO base común y luego extenderlo, O si decides
    // poner todos los campos posibles aquí y dejarlos null si no aplican.
    // Ejemplo si pones todos aquí:
    // private String autor;
    // private String editorial;
    // etc...

    // --- Constructor Simplificado (Ejemplo) ---
    // Puedes añadir constructores específicos si los necesitas.
    public ProductoDTO(UUID id, String descripcion, BigDecimal precio, ProveedorDTO proveedor, Set<CategoriaDTO> categoria, Integer unidades) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.unidades = unidades;
    }

    public ProductoDTO(UUID id, String nombre, BigDecimal precio, String descripcion, CategoriaDTO categoria) {
    }
}