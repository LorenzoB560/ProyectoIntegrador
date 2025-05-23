package org.grupob.adminapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.dto.DepartamentoDTO;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.adminapp.dto.auxiliar.GeneroDTO;
import org.grupob.comun.dto.PeriodoDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificacionEmpleadoDTO {

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