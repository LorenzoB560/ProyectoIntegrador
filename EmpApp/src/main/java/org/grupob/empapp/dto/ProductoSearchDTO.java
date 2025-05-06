package org.grupob.empapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Getters, Setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSearchDTO {

    private String tipo;   // Para filtrar por "Libro", "Electrónico", "Ropa", etc. o "" para Todos
    private String nombre; // Para filtrar por nombre (usará LIKE)
    private Double precio; // Para filtrar por precio exacto (nullable)


}