package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.empapp.entity.auxiliar.DireccionPostal;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AltaEmpleadoDTO {

    private UUID id;

    // ** PASO 1 - DATOS PERSONALES **
    public interface GrupoPersonal {};
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Long idGeneroSeleccionado;

    // ** PASO 2 - DATOS DIRECCION **
    public interface GrupoDireccion{}

    private DireccionPostalDTO direccion;

    // ** PASO 3 - DATOS LABORALES **
    public interface GrupoLaboral{}

    private UUID idDepartamentoSeleccionado;

    public interface GrupoFotoPerfil{}
    // ** PASO 4 - FOTO DE PERFIL **
    private byte[] foto; // Para almacenar la imagen en la base de datos
}
