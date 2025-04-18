package org.grupob.empapp.repository;

import org.grupob.empapp.entity.UsuarioEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioEmpleadoRepository extends JpaRepository<UsuarioEmpleado, UUID> {
}
