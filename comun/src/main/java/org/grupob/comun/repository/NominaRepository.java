package org.grupob.comun.repository;

import org.grupob.comun.entity.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, UUID> {
}
