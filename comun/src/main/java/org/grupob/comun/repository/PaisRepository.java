package org.grupob.comun.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.grupob.comun.entity.maestras.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {
    boolean existsPaisByPrefijo(String prefijo);
    boolean existsPaisByPais(String pais);

    Optional<Pais> findPaisByPais(String pais);

}
