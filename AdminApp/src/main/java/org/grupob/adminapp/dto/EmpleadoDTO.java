package org.grupob.adminapp.dto; // O el paquete que prefieras para tus DTOs

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.dto.DepartamentoDTO;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.adminapp.dto.auxiliar.GeneroDTO;
import org.grupob.comun.dto.PeriodoDTO;

// Importa otros DTOs necesarios (EspecialidadDto, DepartamentoDto, etc.)

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {

    // Campos heredados de Persona (o los que quieras exponer)
    private UUID id;
    private String nombre;
    private String apellido;
    private GeneroDTO genero;
    private LocalDate fechaNacimiento;
    private String correo;

    // Campos propios de Empleado
    private String comentarios;
    private Set<EspecialidadDTO> especialidades;
    private UUID idJefe;
    private String nombreJefe;
    private PeriodoDTO periodo;
    private boolean activo;
    private DepartamentoDTO departamento;
    private LoginUsuarioEmpleadoDTO usuario;

    // Campos de informacion_economica
    private BigDecimal salario;
    private BigDecimal comision;

    private CuentaBancariaDTO cuentaCorriente;
    private EntidadBancariaDTO entidadBancaria;
    private TipoTarjetaCreditoDTO idTipoTarjeta;
    private TarjetaCreditoDTO tarjetaCredito;

    private boolean tieneFoto;

    private Set<EtiquetaDTO> etiquetas = new HashSet<>();
}