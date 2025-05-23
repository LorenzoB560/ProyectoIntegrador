package org.grupob.comun.repository;

import org.grupob.comun.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, UUID> {

    Optional<Departamento> findDepartamentoByNombre(String nombre);

    boolean existsDepartamentoById(UUID id);
}
