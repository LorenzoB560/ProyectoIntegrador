package org.grupob.comun.repository;

import org.grupob.comun.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, UUID> {

    List<Etiqueta> findByCreador_IdOrderByNombreAsc(UUID creadorId);

    Optional<Etiqueta> findByNombreIgnoreCaseAndCreador_Id(String nombre, UUID creadorId);

    @Query("SELECT e FROM Etiqueta e WHERE e.creador.id = :creadorId AND LOWER(e.nombre) LIKE LOWER(CONCAT(:prefijo, '%')) ORDER BY e.nombre ASC")
    List<Etiqueta> findByCreadorIdAndNombreStartingWithIgnoreCaseOrderByNombreAsc(
            @Param("creadorId") UUID creadorId,
            @Param("prefijo") String prefijo
    );
}