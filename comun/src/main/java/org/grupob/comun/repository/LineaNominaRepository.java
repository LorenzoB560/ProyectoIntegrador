package org.grupob.comun.repository;

import org.grupob.comun.entity.LineaNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LineaNominaRepository extends JpaRepository<LineaNomina, UUID> {
}
