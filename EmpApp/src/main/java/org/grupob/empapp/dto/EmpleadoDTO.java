package org.grupob.empapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.Usuario;
import org.grupob.empapp.validation.email.EmailValidado;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {

    private UUID id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    // La base de datos no tiene DNI, por lo tanto lo comento
    //private String dni;

    //Añadida validación personalizada para que el email sea válido.
    @EmailValidado
    private String email;
    private String telefonoMovil;
    private LocalDate fechaContratacion;
    private LocalDate fechaCese;

    //TODO    private Departamento departamento; REVISAR COMO IMPLEMENTAR
    private UUID idJefe;
    private String nombreJefe;
    private String apellidoJefe;
    private Long idDepartamento;
    private String nombreDepartamento;



    private Usuario usuario;

    private LocalDate fechaEliminacion;

    private LocalDate fechaInsercion;




}
