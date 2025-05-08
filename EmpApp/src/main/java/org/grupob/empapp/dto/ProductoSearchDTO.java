package org.grupob.empapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List; // Para la lista de IDs de categoría

@Data               // Genera Getters, Setters, toString, equals, hashCode
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class ProductoSearchDTO {

    // Criterios de búsqueda basados en el PDF (Sección 3.2)

    /**
     * Patrón a buscar dentro de la descripción del producto (usará LIKE).
     * Si es null o vacío, no se filtra por descripción.
     */
    private String descripcionPatron;

    /**
     * ID del proveedor seleccionado.
     * Si es null, se buscan productos de cualquier proveedor.
     */
    private Long idProveedor; // Asumiendo que el ID de Proveedor es Long

    /**
     * Lista de IDs de las categorías seleccionadas.
     * Se buscarán productos que pertenezcan a CUALQUIERA de estas categorías (OR / IN).
     * Si la lista es null o vacía, se buscan productos de cualquier categoría.
     */
    private List<Long> idsCategorias; // Asumiendo que el ID de Categoria es Long

    /**
     * Filtro para indicar si el producto es segundaMano.
     * Puede ser true (buscar solo segundaMano), false (buscar solo no segundaMano),
     * o null (buscar todos, sin importar si son segundaMano o no).
     */
    private Boolean segundaMano;

    // --- Filtros Adicionales (Opcionales, basados en implementaciones anteriores) ---

    /**
     * Precio mínimo del producto (inclusive).
     * Si es null, no hay límite inferior de precio.
     */
    private BigDecimal precioMin;

    /**
     * Precio máximo del producto (inclusive).
     * Si es null, no hay límite superior de precio.
     */
    private BigDecimal precioMax;

    // Podrías añadir otros como 'nombre', 'marca', etc., si los necesitas

}