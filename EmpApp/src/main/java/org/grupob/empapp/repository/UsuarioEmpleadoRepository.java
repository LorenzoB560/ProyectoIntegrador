package org.grupob.empapp.repository;

import org.grupob.empapp.entity.UsuarioEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioEmpleadoRepository extends JpaRepository<UsuarioEmpleado, UUID> {
    Optional<UsuarioEmpleado> findByCorreo(String correo);
}
