package org.grupob.comun.repository;

import org.grupob.comun.entity.Nomina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, UUID> {

    //Esto sirve para recalcular el total del líquido en una nómina cuando se elimina un concepto
    @Modifying
    @Query("UPDATE Nomina n SET n.totalLiquido = :nuevoTotal WHERE n.id = :idNomina")
    void updateTotalLiquido(@Param("idNomina") UUID idNomina, @Param("nuevoTotal") BigDecimal nuevoTotal);

    @Query("""
    SELECT n FROM Nomina n
    WHERE (:nombre IS NULL OR 
           LOWER(CONCAT(n.empleado.nombre, ' ', n.empleado.apellido)) LIKE LOWER(CONCAT('%', :nombre, '%')))
      AND (:fechaInicio IS NULL OR n.periodo.fechaInicio = :fechaInicio)
      AND (:fechaFin IS NULL OR n.periodo.fechaFin = :fechaFin)
      AND (:conceptos IS NULL OR EXISTS (
            SELECT 1 FROM LineaNomina ln
            WHERE ln.nomina = n
              AND ln.concepto.nombre IN :conceptos
      ))
""")
    Page<Nomina> buscarNominasFiltradas(
            @Param("nombre") String nombre,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("conceptos") List<String> conceptos,
            Pageable pageable
    );


    Page<Nomina> findNominaByEmpleadoId(UUID id, Pageable pageable);

    @Query("""
    SELECT n FROM Nomina n
    WHERE n.empleado.id = :idEmpleado
      AND (:fechaInicio IS NULL OR n.periodo.fechaInicio = :fechaInicio)
      AND (:fechaFin IS NULL OR n.periodo.fechaFin = :fechaFin)
      AND (:conceptos IS NULL OR EXISTS (
            SELECT 1 FROM LineaNomina ln
            WHERE ln.nomina = n
              AND ln.concepto.nombre IN :conceptos
      ))
""")
    Page<Nomina> buscarNominasFiltradasPorEmpleado(
            @Param("idEmpleado") UUID idEmpleado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("conceptos") List<String> conceptos,
            Pageable pageable
    );
}
