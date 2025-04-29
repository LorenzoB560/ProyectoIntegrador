package org.grupob.comun.repository;

import org.grupob.comun.entity.maestras.TipoTarjetaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTarjetaRepository extends JpaRepository<TipoTarjetaCredito, Long> {
    boolean existsTipoTarjetaCreditoById(Long id);
}
