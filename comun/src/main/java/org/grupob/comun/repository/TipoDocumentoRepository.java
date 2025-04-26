package org.grupob.comun.repository;

import org.grupob.comun.entity.maestras.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    boolean existsByDocumento(String tipo);
}
