package org.grupob.comun.repository.maestras;

import org.grupob.comun.entity.maestras.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor,  Long> {
    Optional<Proveedor> findByNombre(String nombre);
}
