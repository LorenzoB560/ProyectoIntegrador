package org.grupob.adminapp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductoDTO {
    private UUID id;
    private String nombre;
    private Double precio;
    private String descripcion;

    // Campo para identificar el tipo específico
    private String tipoProductoNombre; // Ej: "Libro", "Electrónico", "Ropa"

    // Campos específicos (serán null si no aplican al tipo)
    private String autor;
    private String editorial;
    private String marca;
    private String material;


}