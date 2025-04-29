package org.grupob.comun.repository.maestras;

import org.grupob.comun.entity.maestras.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero,  Long> {
    boolean existsGeneroById(Long id);
}
