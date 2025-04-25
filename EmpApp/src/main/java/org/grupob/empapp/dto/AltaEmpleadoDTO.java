package org.grupob.empapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosProfesionales;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosPersonales;
import org.grupob.comun.validation.fechas.LocalDateNotBlank;
import org.grupob.comun.validation.fechas.MayorDe18;
import org.grupob.empapp.validation.edad.EdadCoincideConFechaNacimiento;
import org.grupob.empapp.validation.edad.EdadNotBlank;
import org.grupob.empapp.validation.pais.ExistePais;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EdadCoincideConFechaNacimiento(groups = GrupoDatosPersonales.class)
public class AltaEmpleadoDTO {

    private UUID id;

    // ** PASO 1 - DATOS PERSONALES **

    @NotNull(groups = GrupoDatosPersonales.class)
    @NotBlank(groups = GrupoDatosPersonales.class)
    private String nombre;

    @NotNull(groups = GrupoDatosPersonales.class)
    @NotBlank(groups = GrupoDatosPersonales.class)
    private String apellido;

//    @NotNull(groups = GrupoPersonal.class)
//    //TODO FALTAN REALIZAR LAS VALIDACIONES AQUÍ
//    private byte[] foto; // Para almacenar la imagen en la base de datos

    @NotNull(groups = GrupoDatosPersonales.class)
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
    @NotBlank(groups = GrupoDatosPersonales.class)
    private String comentarios;
    // ** PASO 2 - DATOS DIRECCION **
    @Valid
    private DireccionPostalDTO direccion;

    // ** PASO 3 - DATOS LABORALES **
    @NotNull(groups = GrupoDatosProfesionales.class)
    private UUID idDepartamentoSeleccionado;

    // ** PASO 4 - FOTO DE PERFIL **
}
