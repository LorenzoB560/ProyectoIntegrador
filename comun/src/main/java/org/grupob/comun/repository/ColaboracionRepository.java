package org.grupob.comun.repository;


import org.grupob.comun.entity.Colaboracion;
import org.grupob.comun.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColaboracionRepository extends JpaRepository<Colaboracion, UUID> {
    // Busca una colaboración existente entre dos empleados, sin importar quién es emisor o receptor
    @Query("SELECT c FROM Colaboracion c WHERE (c.emisor = :empleado1 AND c.receptor = :empleado2) OR (c.emisor = :empleado2 AND c.receptor = :empleado1)")
    Optional<Colaboracion> findColaboracionEntreEmpleados(@Param("empleado1") Empleado empleado1, @Param("empleado2") Empleado empleado2);
    @Query("SELECT c FROM Colaboracion c WHERE c.emisor.id = :empleadoId OR c.receptor.id = :empleadoId")
    List<Colaboracion> findAllByEmpleadoId(@Param("empleadoId") UUID empleadoId);
}