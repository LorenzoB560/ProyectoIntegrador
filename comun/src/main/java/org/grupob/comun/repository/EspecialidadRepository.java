package org.grupob.comun.repository;

import org.grupob.comun.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, UUID> {
}
