package org.grupob.empapp.dto;

// ... otras importaciones ...
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.auxiliar.GeneroDTO; // Asegúrate de que existan estos DTOs

@Data
@NoArgsConstructor
// @AllArgsConstructor // Puede que necesites quitar o ajustar el @AllArgsConstructor de Lombok
public class EmpleadoDTO {

    private UUID id;
    private String nombre;
    private String apellido;
    private GeneroDTO genero;
    private LocalDate fechaNacimiento;
    private String correo;
    private String comentarios;
    private Set<EspecialidadDTO> especialidades = new HashSet<>(); // Se llenará después si es necesario
    private UUID idJefe;
    private String nombreJefe;
    private PeriodoDTO periodo;
    private boolean activo;
    private DepartamentoDTO departamento;
    private BigDecimal salario;
    private BigDecimal comision;
    private CuentaBancariaDTO cuentaCorriente;
    private EntidadBancariaDTO entidadBancaria;
    private TipoTarjetaCreditoDTO tipoTarjetaCredito;
    private TarjetaCreditoDTO tarjetaCredito;
    private boolean tieneFoto;
    private Set<EtiquetaDTO> etiquetas = new HashSet<>(); // Se llenará después si es necesario

    // *** CONSTRUCTOR PARA LA PROYECCIÓN JPA ***
    public EmpleadoDTO(UUID id, String nombre, String apellido, GeneroDTO genero, LocalDate fechaNacimiento,
                       String correo, String comentarios, UUID idJefe, String nombreJefe, PeriodoDTO periodo,
                       boolean activo, DepartamentoDTO departamento, BigDecimal salario, BigDecimal comision,
                       CuentaBancariaDTO cuentaCorriente, EntidadBancariaDTO entidadBancaria,
                       TipoTarjetaCreditoDTO tipoTarjetaCredito, TarjetaCreditoDTO tarjetaCredito, boolean tieneFoto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.comentarios = comentarios;
        this.idJefe = idJefe;
        this.nombreJefe = nombreJefe;
        this.periodo = periodo;
        this.activo = activo;
        this.departamento = departamento;
        this.salario = salario;
        this.comision = comision;
        this.cuentaCorriente = cuentaCorriente;
        this.entidadBancaria = entidadBancaria;
        this.tipoTarjetaCredito = tipoTarjetaCredito;
        this.tarjetaCredito = tarjetaCredito;
        this.tieneFoto = tieneFoto;
        // Las colecciones se inicializan vacías por defecto
        this.especialidades = new HashSet<>();
        this.etiquetas = new HashSet<>();
    }

    // Puedes mantener el @AllArgsConstructor si lo necesitas para otras cosas,
    // pero asegúrate de que el constructor específico para JPA exista.
}