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

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Importa UUID

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {


    // Elimina por categoría usando JPQL
    @Modifying
    @Query("DELETE FROM Producto p WHERE p IN " +
            "(SELECT p2 FROM Producto p2 JOIN p2.categoria c WHERE c.id = :categoriaId)")
    void deleteByCategoriaId(@Param("categoriaId") Long categoriaId);

    boolean existsByDescripcionAndProveedorNombre(String descripcion, String nombreProveedor);
    Optional<Producto> findByDescripcionAndProveedorNombre(String descripcion, String nombreProveedor);

    @Query("SELECT p FROM Producto p WHERE TYPE(p) = :clase")
    <T extends Producto> List<T> findByType(@Param("clase") Class<T> clase);




/*; // --- Consulta Base (solo campos comunes) ---
    @Query("SELECT p FROM Producto p WHERE " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:precio IS NULL OR p.precio = :precio)")
    Page<Producto> buscarProductosBasePaginado(
            @Param("nombre") String nombre,
            @Param("precio") Double precio,
            Pageable pageable);

    // --- Consulta Específica para Producto1 (Libro) ---
    @Query("SELECT p1 FROM Libro p1 WHERE " +
            "(:nombre IS NULL OR LOWER(p1.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:precio IS NULL OR p1.precio = :precio)")
    Page<Libro> buscarLibroPaginado(
            @Param("nombre") String nombre,
            @Param("precio") Double precio,
            // @Param("autor") String autor, // <-- Ejemplo si añades filtro por autor
            Pageable pageable);

    // --- Consulta Específica para Producto2 (Electrónico) ---
    @Query("SELECT p2 FROM Electronico p2 WHERE " +
            "(:nombre IS NULL OR LOWER(p2.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:precio IS NULL OR p2.precio = :precio)")
    // Añadir filtros específicos de Producto2 si es necesario
    Page<Electronico> buscarElectronicoPaginado(
            @Param("nombre") String nombre,
            @Param("precio") Double precio,
            // @Param("marca") String marca, // <-- Ejemplo
            Pageable pageable);

    // --- Consulta Específica para Producto3 (Ropa) ---
    @Query("SELECT p3 FROM Mueble p3 WHERE " +
            "(:nombre IS NULL OR LOWER(p3.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:precio IS NULL OR p3.precio = :precio)")
    // Añadir filtros específicos de Producto3 si es necesario
    Page<Mueble> buscarRopaPaginado(
            @Param("nombre") String nombre,
            @Param("precio") Double precio,
            // @Param("material") String material, // <-- Ejemplo
            Pageable pageable);
*/
}