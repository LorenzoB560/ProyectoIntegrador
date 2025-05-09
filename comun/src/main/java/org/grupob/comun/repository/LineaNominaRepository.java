package org.grupob.comun.repository;

import org.grupob.comun.entity.LineaNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface LineaNominaRepository extends JpaRepository<LineaNomina, UUID> {
    void deleteLineaNominaByConceptoId(UUID idConcepto);

    @Query("SELECT COALESCE(SUM(l.cantidad), 0) FROM LineaNomina l WHERE l.nomina.id = :idNomina")
    BigDecimal sumCantidadByIdNomina(@Param("idNomina") UUID idNomina);

    @Query("SELECT COALESCE(SUM(l.cantidad), 0) FROM LineaNomina l WHERE l.nomina.id = :idNomina AND l.concepto.tipo = 'INGRESO'")
    BigDecimal getTotalIngresos(@Param("idNomina") UUID idNomina);

    @Query("SELECT COALESCE(SUM(l.cantidad), 0) FROM LineaNomina l WHERE l.nomina.id = :idNomina AND l.concepto.tipo = 'DEDUCCION'")
    BigDecimal getTotalDeducciones(@Param("idNomina") UUID idNomina);


    @Modifying
    @Query("DELETE FROM LineaNomina ln WHERE ln.nomina.id = :idNomina AND ln.concepto.id = :idConcepto")
    void deleteLineaNominaByNominaIdAndConceptoId(@Param("idNomina") UUID idNomina, @Param("idConcepto") UUID idConcepto);

}
