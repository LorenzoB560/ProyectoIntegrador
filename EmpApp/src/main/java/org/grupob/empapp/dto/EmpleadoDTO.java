package org.grupob.empapp.dto; // O el paquete que prefieras para tus DTOs

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.*;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.auxiliar.GeneroDTO;

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
    // ... otros campos de Persona que necesites ...
    private String correo;

    // Campos propios de Empleado
    private String comentarios;
    private Set<EspecialidadDTO> especialidades; // Asume que tienes un EspecialidadDto
    private UUID idJefe; // O un EmpleadoSimpleDto para evitar recursión
    private String nombreJefe; // Podrías necesitar mapeo customizado
    private PeriodoDTO periodo;
    private boolean activo;
    private DepartamentoDTO departamento; // Asume que tienes un DepartamentoDto
    private LoginUsuarioEmpleadoDTO usuario; // Asume que tienes un UsuarioEmpleadoDto

    // Campos de informacion_economica
    private BigDecimal salario;
    private BigDecimal comision;

    private CuentaBancariaEmpleadoDTO cuentaCorriente;
    private EntidadBancariaDTO entidadBancaria; // Asume que tienes un EntidadBancariaDto
    private TipoTarjetaCreditoDTO idTipoTarjeta; // Asume que tienes un TipoTarjetaCreditoDto
    private TarjetaCreditoDTO tarjetaCredito;

    // La foto (byte[]) podría no ser ideal para un DTO estándar.
    // Podrías omitirla, devolver un booleano indicando si existe, o una URL para descargarla.
    // private byte[] foto;
    private boolean tieneFoto;

    private Set<EtiquetaDTO> etiquetas = new HashSet<>();
}