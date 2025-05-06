package org.grupob.comun.repository;

import org.grupob.comun.entity.Electronico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ElectronicoRepository extends JpaRepository<Electronico, UUID> {


}
