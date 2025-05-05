package org.grupob.comun.repository;

import org.grupob.comun.entity.Libro;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    // Elimina por categoría usando JPQL
    @Modifying
    @Query("DELETE FROM Producto p WHERE p.categoria.id = :categoriaId")
    void deleteByCategoriaId(@Param("categoriaId") Long categoriaId);

}
