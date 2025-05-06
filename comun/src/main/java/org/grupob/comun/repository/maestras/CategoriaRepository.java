package org.grupob.comun.repository.maestras;

import org.grupob.comun.entity.maestras.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,  Long> {
}
