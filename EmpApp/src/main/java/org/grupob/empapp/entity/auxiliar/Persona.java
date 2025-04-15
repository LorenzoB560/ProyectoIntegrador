package org.grupob.empapp.entity.auxiliar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data

@MappedSuperclass
public class Persona {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    private String nombre;

    private String apellido;

    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private Genero genero;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipo", column = @Column(name = "tipo_via")),
    })
    private DireccionPostal direccion;

}
