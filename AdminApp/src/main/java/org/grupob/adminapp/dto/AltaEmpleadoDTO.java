package org.grupob.adminapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.validation.fechas.LocalDateNotBlank;
import org.grupob.comun.validation.fechas.MayorDe18;
import org.grupob.adminapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.adminapp.dto.grupoValidaciones.GrupoLaboral;
import org.grupob.adminapp.dto.grupoValidaciones.GrupoPersonal;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AltaEmpleadoDTO {

    private UUID id;

    // ** PASO 1 - DATOS PERSONALES **
    @NotBlank(groups = GrupoPersonal.class)
    private String nombre;
    @NotBlank(groups = GrupoPersonal.class)
    private String apellido;

    @NotNull(groups = GrupoPersonal.class)
    @LocalDateNotBlank(groups = GrupoPersonal.class)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Past(message = "{registro.fechaNacimiento.past", groups = GrupoPersonal.class)
    @MayorDe18(groups = GrupoPersonal.class)
    private LocalDate fechaNacimiento;

    @NotNull(groups = GrupoPersonal.class)
    private Long idGeneroSeleccionado;

    // ** PASO 2 - DATOS DIRECCION **
    @Valid
    private DireccionPostalDTO direccion;

    // ** PASO 3 - DATOS LABORALES **
    @NotNull(groups = GrupoLaboral.class)
    private UUID idDepartamentoSeleccionado;

    // ** PASO 4 - FOTO DE PERFIL **
    private byte[] foto; // Para almacenar la imagen en la base de datos
}
