package org.grupob.comun.repository;

import org.grupob.comun.entity.Empleado;
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
    // Buscar empleado por nombre (método de la nueva entidad)
    Empleado getEmpleadoByNombre(String nombre);

    // Método existente adaptado a la nueva entidad
    Optional<Empleado> findEmpleadoByNombre(String nombre);

    // Método para búsqueda avanzada con paginación y ordenación - adaptado a la nueva estructura
    @Query("SELECT e FROM Empleado e LEFT JOIN e.departamento d WHERE " +
            "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:departamento IS NULL OR LOWER(d.nombre) LIKE LOWER(CONCAT('%', :departamento, '%'))) AND " +
            "(:comentarios IS NULL OR LOWER(e.comentarios) LIKE LOWER(CONCAT('%', :comentarios, '%'))) AND " +
            "(:fechaLimite IS NULL OR e.periodo.fechaInicio < :fechaLimite) AND " +
            "(:salarioMin IS NULL OR e.salario >= :salarioMin)")
    Page<Empleado> buscarEmpleadosAvanzadoPaginado(
            @Param("nombre") String nombre,
            @Param("departamento") String departamento,
            @Param("comentarios") String comentarios,
            @Param("fechaLimite") LocalDate fechaLimite,
            @Param("salarioMin") BigDecimal salarioMin,
            Pageable pageable);

    // Métodos de búsqueda individuales - adaptados a la nueva estructura
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT e FROM Empleado e JOIN e.departamento d WHERE LOWER(d.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Empleado> findByDepartamentoNombreContaining(@Param("nombre") String nombre);

    List<Empleado> findByComentariosContainingIgnoreCase(String comentarios);

    @Query("SELECT e FROM Empleado e WHERE e.periodo.fechaInicio < :fecha")
    List<Empleado> findByFechaContratacionBefore(LocalDate fecha);

    List<Empleado> findBySalarioGreaterThanEqual(BigDecimal salarioMinimo);

    // Encontrar subordinados de un jefe
    List<Empleado> findByJefe_Id(UUID jefeId);

    // Encontrar empleados por especialidad
    @Query("SELECT e FROM Empleado e JOIN e.especialidades esp WHERE esp.id = :especialidadId")
    List<Empleado> findByEspecialidadId(@Param("especialidadId") UUID especialidadId);

    // Buscar empleados activos
    List<Empleado> findByActivoTrue();

    // Buscar por entidad bancaria
    List<Empleado> findByEntidadBancaria_Id(UUID entidadBancariaId);

    // Buscar por rango salarial
    List<Empleado> findBySalarioBetween(BigDecimal minSalario, BigDecimal maxSalario);
    // Encontrar empleados por etiqueta
// @Query("SELECT e FROM Empleado e JOIN e.empleadoEtiquetas ee WHERE ee.etiqueta.id = :etiquetaId")
// List<Empleado> findByEtiquetaId(@Param("etiquetaId") UUID etiquetaId);
}