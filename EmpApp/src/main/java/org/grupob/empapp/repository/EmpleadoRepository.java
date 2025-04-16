package org.grupob.empapp.repository;

import org.grupob.empapp.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
}
