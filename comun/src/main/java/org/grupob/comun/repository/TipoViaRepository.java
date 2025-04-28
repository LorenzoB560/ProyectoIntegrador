package org.grupob.comun.repository;

import org.grupob.comun.entity.maestras.TipoVia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoViaRepository extends JpaRepository<TipoVia, Long> {
    boolean existsByTipoVia(String via);
}
