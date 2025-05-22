package org.grupob.comun.repository;

import org.grupob.comun.entity.UsuarioEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioEmpleadoRepository extends JpaRepository<UsuarioEmpleado, UUID> {
    Optional<UsuarioEmpleado> findByUsuario(String correo);

    List<UsuarioEmpleado> findByActivoFalseAndMotivoBloqueoIsNotNull();

    List<UsuarioEmpleado> findByActivoFalseAndFechaDesbloqueoIsNotNullAndFechaDesbloqueoLessThanEqual(LocalDateTime ahora);
}
