package org.grupob.comun.repository;

import org.grupob.comun.entity.Libro;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    // Consultas comunes a todos los productos
    List<Producto> findByCategoriaNombre(String nombreCategoria);

    // Consulta polimórfica
    @Query("SELECT p FROM Producto p WHERE TYPE(p) = Libro")
    List<Libro> findAllLibros();

}
