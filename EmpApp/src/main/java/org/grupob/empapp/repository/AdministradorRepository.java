package org.grupob.empapp.repository;

import org.grupob.empapp.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdministradorRepository extends JpaRepository<Administrador, UUID> {
}
