package org.grupob.comun.repository.maestras;

import org.grupob.comun.entity.maestras.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma,  Long> {
}
