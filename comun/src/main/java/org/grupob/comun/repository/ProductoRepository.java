package org.grupob.comun.repository;

import org.grupob.comun.dto.ProductoSearchDTO;
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
import java.util.Optional;
import java.util.UUID; // Importa UUID

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    boolean existsByDescripcionAndProveedorNombre(String descripcion, String nombreProveedor);
    Optional<Producto> findByDescripcionAndProveedorNombre(String descripcion, String nombreProveedor);


    // 1.1. Obtener los IDs de los productos asociados a una categoría
    @Query("SELECT p.id FROM Producto p JOIN p.categoria c WHERE c.id = :categoriaId")
    List<UUID> findIdsByCategoriaId(@Param("categoriaId") Long categoriaId);

    // 1.2. Eliminar productos por una lista de IDs
    @Modifying
    @Query("DELETE FROM Producto p WHERE p.id IN :ids")
    void deleteByIds(@Param("ids") List<UUID> ids);

    // Elimina por categoría usando JPQL
    @Modifying
    @Query("DELETE FROM Producto p WHERE p IN " +
            "(SELECT p2 FROM Producto p2 JOIN p2.categoria c WHERE c.id = :categoriaId)")
    void deleteByCategoriaId(@Param("categoriaId") Long categoriaId);


    @Query("SELECT DISTINCT p FROM Producto p " +
            "LEFT JOIN p.proveedor prov " +
            "LEFT JOIN p.categoria cat " +
            "WHERE " +
            // Filtro por Descripción
            // Se añade '= true' a la comprobación de nulidad de SpEL
            "(:#{#searchParams.descripcionPattern == null} = true OR TRIM(:#{#searchParams.descripcionPattern}) = '' OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', TRIM(:#{#searchParams.descripcionPattern}), '%'))) AND " +

            // Filtro por Proveedor
            // Se añade '= true' a la comprobación de nulidad de SpEL
            "(:#{#searchParams.proveedorId == null} = true OR prov.id = :#{#searchParams.proveedorId}) AND " +

            // Filtro por Categorías (ya estaba correcto con '= true')
            "(:#{#searchParams.idsCategorias == null or #searchParams.idsCategorias.isEmpty()} = true OR cat.id IN (:#{#searchParams.idsCategorias})) AND " +

            // Filtro por Segunda Mano
            // Se añade '= true' a la comprobación de nulidad de SpEL
            "(:#{#searchParams.esSegundaMano == null} = true OR p.segundaMano = :#{#searchParams.esSegundaMano}) AND " +

            // Filtro por Precio Mínimo
            // Se añade '= true' a la comprobación de nulidad de SpEL
            "(:#{#searchParams.precioMin == null} = true OR p.precio >= :#{#searchParams.precioMin}) AND " +

            // Filtro por Precio Máximo
            // Se añade '= true' a la comprobación de nulidad de SpEL
            "(:#{#searchParams.precioMax == null} = true OR p.precio <= :#{#searchParams.precioMax})")
    Page<Producto> buscarProductosAdminPaginado(
            @Param("searchParams") ProductoSearchDTO searchParams,
            Pageable pageable);

}