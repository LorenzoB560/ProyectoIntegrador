package org.grupob.adminapp.repository;

import org.grupob.adminapp.entity.UsuarioEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioEmpleadoRepository extends JpaRepository<UsuarioEmpleado, UUID> {
}
