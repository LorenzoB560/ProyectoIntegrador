package org.grupob.adminapp.repository;

import org.grupob.adminapp.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdministradorRepository extends JpaRepository<Administrador, UUID> {
}
