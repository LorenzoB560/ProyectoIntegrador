package org.grupob.comun.repository.maestras;

import org.grupob.comun.entity.maestras.Genero;
import org.grupob.comun.entity.maestras.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TallaRepository extends JpaRepository<Talla,  Long> {
    Optional<Talla> findByTalla(String talla);
}
