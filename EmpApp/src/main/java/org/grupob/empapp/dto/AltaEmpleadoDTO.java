package org.grupob.empapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.grupob.comun.dto.grupo_validaciones.*;
import org.grupob.comun.entity.Especialidad;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.comun.validation.fechas.LocalDateNotBlank;
import org.grupob.comun.validation.fechas.MayorDe18;
import org.grupob.empapp.validation.departamento.ExisteDepartamento;
import org.grupob.empapp.validation.documento_valido.DocumentoValido;
import org.grupob.empapp.validation.edad.EdadCoincideConFechaNacimiento;
import org.grupob.empapp.validation.edad.EdadNotBlank;
import org.grupob.empapp.validation.especialidades.EspecialidadesMinimas;
import org.grupob.empapp.validation.especialidades.ExisteEspecialidad;
import org.grupob.empapp.validation.genero.ExisteGenero;
import org.grupob.empapp.validation.pais.ExistePais;
import org.grupob.empapp.validation.prefijo.ExistePrefijo;
import org.grupob.empapp.validation.tarjeta_bancaria.cvc.CVCValido;
import org.grupob.empapp.validation.terminos.AceptarTerminos;
import org.grupob.empapp.validation.tipo_documento.ExisteTipoDocumento;
import org.grupob.empapp.validation.tarjeta_bancaria.tipo_tarjeta.ExisteTipoTarjeta;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@EdadCoincideConFechaNacimiento(groups = GrupoDatosPersonales.class)
@DocumentoValido(groups = GrupoDatosContacto.class)
@CVCValido(groups = GrupoDatosEconomicos.class)
public class AltaEmpleadoDTO {

    public AltaEmpleadoDTO(){
        this.idGeneroSeleccionado = 2L;
        this.paisNacimiento = "España";
        this.tipoDocumento = "DNI";
        this.prefijoTelefono = "+34";
    }

    private UUID id;
    // ** PASO 1 - DATOS PERSONALES **

    @NotNull(groups = GrupoDatosPersonales.class)
    @NotBlank(groups = GrupoDatosPersonales.class)
    private String nombre;

    @NotNull(groups = GrupoDatosPersonales.class)
    @NotBlank(groups = GrupoDatosPersonales.class)
    private String apellido;

    private byte[] foto; // Para almacenar la imagen en la base de datos


//    @NotNull(groups = GrupoDatosPersonales.class)
//    @Size(min = 1, message = "{foto.message}", groups = GrupoDatosPersonales.class)
    private MultipartFile archivoFoto;

    @NotNull(groups = GrupoDatosPersonales.class)
    @ExisteGenero(groups = GrupoDatosPersonales.class)
    private Long idGeneroSeleccionado;

    @NotNull(groups = GrupoDatosPersonales.class)
    @LocalDateNotBlank(groups = GrupoDatosPersonales.class)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Past(message = "{registro.fechaNacimiento.past}", groups = GrupoDatosPersonales.class)
    @MayorDe18(groups = GrupoDatosPersonales.class)
    private LocalDate fechaNacimiento;


    @NotNull(groups = GrupoDatosPersonales.class)
    @EdadNotBlank(groups = GrupoDatosPersonales.class)
    private String edad;

    @NotNull(groups = GrupoDatosPersonales.class)
    @NotBlank(groups = GrupoDatosPersonales.class)
    @ExistePais(groups = GrupoDatosPersonales.class)
    private String paisNacimiento;

    @NotNull(groups = GrupoDatosPersonales.class)
    private String comentarios;

    // ** PASO 2 - DATOS CONTACTO **
    @NotNull(groups = GrupoDatosContacto.class)
    @ExisteTipoDocumento(groups = GrupoDatosContacto.class)
    private String tipoDocumento;

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    private String numDocumento;

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    @ExistePrefijo(groups = GrupoDatosContacto.class)
    private String prefijoTelefono;

    @NotNull(groups = GrupoDatosContacto.class)
    @NotBlank(groups = GrupoDatosContacto.class)
    @Pattern(regexp = "^\\d{9}$", message = "{NumeroTelefono.message}", groups = GrupoDatosContacto.class)
    private String numTelefono;

    @Valid
    private DireccionPostalDTO direccion;

    // ** PASO 3 - DATOS PROFESIONALES **
    @NotNull(groups = GrupoDatosProfesionales.class)
    @ExisteDepartamento(groups = GrupoDatosProfesionales.class)
    private UUID idDepartamentoSeleccionado;

    @NotNull(groups = GrupoDatosProfesionales.class)
    @ExisteEspecialidad(groups = GrupoDatosProfesionales.class)
    @EspecialidadesMinimas(groups = GrupoDatosProfesionales.class)
    private Set<Especialidad> especialidades;


    // ** PASO 4 - DATOS ECONOMICOS **

    @Valid
    private CuentaBancariaDTO cuentaBancaria;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @Positive(groups = GrupoDatosEconomicos.class)
    @Digits(integer = 8, fraction = 2, groups = GrupoDatosEconomicos.class)
    private Double salario;


    @Positive(groups = GrupoDatosEconomicos.class)
    @Digits(integer = 8, fraction = 2, groups = GrupoDatosEconomicos.class)
    private Double comision;

    @NotNull(groups = GrupoDatosEconomicos.class)
    @ExisteTipoTarjeta(groups = GrupoDatosEconomicos.class)
    private Long idTipoTarjeta;

    @Valid
    private TarjetaCreditoDTO tarjetaCredito;

    // ** PASO 5 - RESUMEN **

    @AceptarTerminos(groups = GrupoResumen.class)
    private String aceptacionTerminos;
}
