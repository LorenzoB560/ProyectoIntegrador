package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.auxiliar.GeneroDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet; // Asegúrate de importar HashSet
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {

    // ... (otros campos existentes) ...
    private UUID id;
    private String nombre;
    private String apellido;
    private GeneroDTO genero;
    private LocalDate fechaNacimiento;
    private String correo; // Asumiendo que lo mapeas desde UsuarioEmpleado
    private String comentarios;
    private Set<EspecialidadDTO> especialidades = new HashSet<>(); // Inicializar
    private UUID idJefe;
    private String nombreJefe;
    private PeriodoDTO periodo;
    private boolean activo;
    private DepartamentoDTO departamento;
    // Eliminamos LoginUsuarioEmpleadoDTO si no es estrictamente necesario en este DTO
    // private LoginUsuarioEmpleadoDTO usuario;
    private BigDecimal salario;
    private BigDecimal comision;
    private CuentaBancariaDTO cuentaCorriente;
    private EntidadBancariaDTO entidadBancaria;
    private TipoTarjetaCreditoDTO tipoTarjetaCredito;
    private TarjetaCreditoDTO tarjetaCredito;
    private boolean tieneFoto;

    // --- NUEVO CAMPO PARA ETIQUETAS ---
    private Set<EtiquetaDTO> etiquetas = new HashSet<>(); // Inicializar colección
}