package org.grupob.adminapp.repository;

import org.grupob.adminapp.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
    Empleado getEmpleadoByNombre(String nombre);
}
