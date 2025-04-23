package org.grupob.empapp.entity.auxiliar.jerarquia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.DireccionPostal;
import org.grupob.empapp.entity.maestras.Genero;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data

@MappedSuperclass
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;

    private String apellido;

    //TODO crear entidad tipo documento
    private String tipoDocumento;

    private String documento;


    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private Integer edad;

    private String paisNacimiento;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_persona_genero_id"))
    private Genero genero;

    //probablemente esto cambie
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipo", column = @Column(name = "tipo_via")),
    })
    private DireccionPostal direccion;

}
