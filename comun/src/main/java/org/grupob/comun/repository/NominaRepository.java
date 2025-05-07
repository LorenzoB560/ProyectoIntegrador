package org.grupob.comun.repository;

import org.grupob.comun.entity.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.UUID;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, UUID> {

    //Esto sirve para recalcular el total del líquido en una nómina cuando se elimina un concepto
    @Modifying
    @Query("UPDATE Nomina n SET n.totalLiquido = :nuevoTotal WHERE n.id = :idNomina")
    void updateTotalLiquido(@Param("idNomina") UUID idNomina, @Param("nuevoTotal") BigDecimal nuevoTotal);
}
