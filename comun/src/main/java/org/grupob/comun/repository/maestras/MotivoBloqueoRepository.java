package org.grupob.comun.repository.maestras;

import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoBloqueoRepository extends JpaRepository<MotivoBloqueo,  Long> {
}
