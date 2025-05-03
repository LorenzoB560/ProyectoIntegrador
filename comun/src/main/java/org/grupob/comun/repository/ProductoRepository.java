package org.grupob.comun.repository;

import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID; // Importa UUID

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID>{
}
