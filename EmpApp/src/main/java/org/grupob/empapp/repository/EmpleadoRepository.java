package org.grupob.empapp.repository;

import org.grupob.empapp.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
    Empleado getEmpleadoByNombre(String nombre);

    // Método existente
   /* Optional<Empleado> findEmpleadoByEname(String ename);

    // Método para búsqueda avanzada con paginación y ordenación
    @Query("SELECT e FROM Empleado e LEFT JOIN e.departamento d WHERE " +
            "(:nombre IS NULL OR LOWER(e.ename) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:departamento IS NULL OR LOWER(d.dname) LIKE LOWER(CONCAT('%', :departamento, '%'))) AND " +
            "(:trabajo IS NULL OR LOWER(e.job) LIKE LOWER(CONCAT('%', :trabajo, '%'))) AND " +
            "(:fechaLimite IS NULL OR e.hiradate < :fechaLimite) AND " +
            "(:salarioMin IS NULL OR e.sal >= :salarioMin)")
    Page<Empleado> buscarEmpleadosAvanzadoPaginado(
            @Param("nombre") String nombre,
            @Param("departamento") String departamento,
            @Param("trabajo") String trabajo,
            @Param("fechaLimite") LocalDate fechaLimite,
            @Param("salarioMin") BigDecimal salarioMin,
            Pageable pageable);

    // Métodos de búsqueda individuales
    List<Empleado> findByEnameContainingIgnoreCase(String ename);

    @Query("SELECT e FROM Empleado e JOIN e.departamento d WHERE LOWER(d.dname) LIKE LOWER(CONCAT('%', :dname, '%'))")
    List<Empleado> findByDepartamentoNombreContaining(@Param("dname") String dname);

    List<Empleado> findByJobContainingIgnoreCase(String job);

    List<Empleado> findByHiradateBefore(LocalDate fecha);

    List<Empleado> findBySalGreaterThanEqual(BigDecimal salarioMinimo);
    // Encontrar subordinados de un jefe
    List<Empleado> findByJefe_Id(UUID jefeId);*/

    // Encontrar empleados por etiqueta
 //   @Query("SELECT e FROM Empleado e JOIN e.empleadoEtiquetas ee WHERE ee.etiqueta.id = :etiquetaId")
  //  List<Empleado> findByEtiquetaId(@Param("etiquetaId") UUID etiquetaId);
}
