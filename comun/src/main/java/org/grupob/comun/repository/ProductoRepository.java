package org.grupob.comun.repository;

import org.grupob.comun.entity.Electronico;
import org.grupob.comun.entity.Libro;
import org.grupob.comun.entity.Mueble;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID; // Importa UUID

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {


    // Elimina por categoría usando JPQL
    @Modifying
    @Query("DELETE FROM Producto p WHERE p IN " +
            "(SELECT p2 FROM Producto p2 JOIN p2.categoria c WHERE c.id = :categoriaId)")
    void deleteByCategoriaId(@Param("categoriaId") Long categoriaId);


    @Query("SELECT DISTINCT p FROM Producto p " +
            "LEFT JOIN p.proveedor prov " + // Join para filtrar por proveedor
            "LEFT JOIN p.categoria cat " +  // Join para filtrar por categorías
            "WHERE " +
            // 1. Filtro por Descripción (LIKE)
            "(:descPatron IS NULL OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :descPatron, '%'))) AND " +
            // 2. Filtro por Proveedor (ID)
            "(:idProv IS NULL OR prov.id = :idProv) AND " +
            // 3. Filtro por Categorías (IN lista de IDs)
            //    Coalesce previene error si :idsCats es null/vacío. Busca productos donde AL MENOS UNA categoría esté en la lista.
            "(:idsCats IS NULL OR cat.id IN :idsCats) AND " +
            // 4. Filtro por segunda mano (Boolean)
            "(:segM IS NULL OR p.segundaMano = :segM) AND " +
            // 5. Filtro Rango Precios (Opcional, si lo mantienes)
            "(:pMin IS NULL OR p.precio >= :pMin) AND " +
            "(:pMax IS NULL OR p.precio <= :pMax)")
    Page<Producto> buscarProductosAdminPaginado(
            @Param("descPatron") String descripcionPatron,
            @Param("idProv") Long idProveedor,
            @Param("idsCats") List<Long> idsCategorias, // La lista de IDs
            @Param("segM") Boolean segundaMano,
            @Param("pMin") BigDecimal precioMin, // Parámetro para precio mínimo
            @Param("pMax") BigDecimal precioMax, // Parámetro para precio máximo
            Pageable pageable); // Spring aplica paginación y ordenación desde aquí

}