package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private byte[] foto; // Para almacenar la imagen en la base de datos

    // ** PASO 2 - DATOS DIRECCION **
    public interface GrupoDireccion{}

    private String tipoVia;
    private String via;
    private String numero;
    private String piso;
    private String puerta;
    private String codigoPostal;
    private String localidad;
    private String region;
    private String pais;

}
