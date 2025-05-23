package org.grupob.comun.repository;

import org.grupob.comun.entity.EntidadBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntidadBancariaRepository extends JpaRepository<EntidadBancaria, UUID> {
    boolean existsEntidadBancariaById(UUID id);

    boolean existsByCodigo(String codigoEntidadBancaria);
}
