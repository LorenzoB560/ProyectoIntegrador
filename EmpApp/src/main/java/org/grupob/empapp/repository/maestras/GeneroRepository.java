package org.grupob.empapp.repository.maestras;

import org.grupob.empapp.entity.maestras.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero,  Long> {
}
