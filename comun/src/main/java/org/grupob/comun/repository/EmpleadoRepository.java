package org.grupob.comun.repository;

import org.grupob.comun.entity.Empleado;
 // *** IMPORTANTE: Importar EmpleadoDTO aquí ***
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

    // --- Métodos existentes (sin cambios si devuelven Entidad) ---
    Empleado getEmpleadoByNombre(String nombre);
    Optional<Empleado> findEmpleadoByNombre(String nombre);
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);
    // ... otros findBy... que devuelven Empleado

    // *** MÉTODO MODIFICADO PARA DTO PROJECTION ***
    @Query("SELECT new org.grupob.empapp.dto.EmpleadoDTO(" + // Paquete completo del DTO
            "e.id, e.nombre, e.apellido, " +
            // Mapeo para GeneroDTO (asumiendo que tienes un constructor adecuado en GeneroDTO o mapeas null)
            "(CASE WHEN g IS NOT NULL THEN NEW org.grupob.empapp.dto.auxiliar.GeneroDTO(g.id, g.genero) ELSE null END), " +
            "e.fechaNacimiento, " +
            // Mapeo para correo (desde UsuarioEmpleado)
            "(CASE WHEN u IS NOT NULL THEN u.usuario ELSE null END), " +
            "e.comentarios, " +
            // Para colecciones como especialidades/etiquetas, no podemos incluirlas directamente en el constructor
            // Las mapearemos después en el servicio o las omitiremos de esta vista de lista.
            // Por ahora las omitimos del constructor DTO para la lista.
            "j.id, j.nombre, " + // IDs y Nombres del jefe
            // Mapeo para PeriodoDTO
            "NEW org.grupob.empapp.dto.PeriodoDTO(e.periodo.fechaInicio, e.periodo.fechaFin), " +
            "e.activo, " +
            // Mapeo para DepartamentoDTO
            "NEW org.grupob.empapp.dto.DepartamentoDTO(d.id, d.codigo, d.nombre, d.localidad), " +
            "e.salario, e.comision, " +
            // Mapeo para CuentaBancariaDTO
            "NEW org.grupob.empapp.dto.CuentaBancariaDTO(e.cuentaCorriente.IBAN), " +
            // Mapeo para EntidadBancariaDTO
            "(CASE WHEN eb IS NOT NULL THEN NEW org.grupob.empapp.dto.EntidadBancariaDTO(eb.id, eb.codigo, eb.nombre) ELSE null END), " +
            // Mapeo para TipoTarjetaCreditoDTO
            "(CASE WHEN tt IS NOT NULL THEN NEW org.grupob.empapp.dto.TipoTarjetaCreditoDTO(tt.id, tt.tipoTarjetaCredito) ELSE null END), " +
            // Mapeo para TarjetaCreditoDTO
            "NEW org.grupob.empapp.dto.TarjetaCreditoDTO(e.tarjetaCredito.numero, e.tarjetaCredito.mesCaducidad, e.tarjetaCredito.anioCaducidad, e.tarjetaCredito.CVC), " +
            "(CASE WHEN e.foto IS NOT NULL THEN true ELSE false END)" + // tieneFoto
            // Nota: Las colecciones (etiquetas, especialidades) se omiten aquí para simplicidad de la proyección.
            // Se podrían cargar por separado si son necesarias en la vista de lista.
            ") " +
            "FROM Empleado e " +
            "LEFT JOIN e.departamento d " +
            "LEFT JOIN e.jefe j " + // LEFT JOIN para que no falle si no tiene jefe
            "LEFT JOIN e.genero g " + // LEFT JOIN si genero puede ser null
            "LEFT JOIN e.usuario u " + // LEFT JOIN si usuario puede ser null
            "LEFT JOIN e.entidadBancaria eb " +
            "LEFT JOIN e.tipoTarjetaCredito tt " +
            "WHERE (:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:departamento IS NULL OR LOWER(d.nombre) LIKE LOWER(CONCAT('%', :departamento, '%'))) AND " + // Usa el alias 'd'
            "(:comentarios IS NULL OR LOWER(e.comentarios) LIKE LOWER(CONCAT('%', :comentarios, '%'))) AND " +
            "(:fechaLimite IS NULL OR e.periodo.fechaInicio < :fechaLimite) AND " +
            "(:salarioMin IS NULL OR e.salario >= :salarioMin)")
    Page<EmpleadoDTO> buscarEmpleadosAvanzadoPaginadoDTO( // Cambiado nombre del método y tipo de retorno
                                                          @Param("nombre") String nombre,
                                                          @Param("departamento") String departamento,
                                                          @Param("comentarios") String comentarios,
                                                          @Param("fechaLimite") LocalDate fechaLimite,
                                                          @Param("salarioMin") BigDecimal salarioMin,
                                                          Pageable pageable);


    // --- Otros métodos ---
    List<Empleado> findByJefe_Id(UUID jefeId);

    @Query("SELECT e FROM Empleado e JOIN FETCH e.especialidades esp WHERE esp.id = :especialidadId") // JOIN FETCH ejemplo
    List<Empleado> findByEspecialidadIdWithFetch(@Param("especialidadId") UUID especialidadId);

    List<Empleado> findByActivoTrue();
    List<Empleado> findByEntidadBancaria_Id(UUID entidadBancariaId);
    List<Empleado> findBySalarioBetween(BigDecimal minSalario, BigDecimal maxSalario);

}