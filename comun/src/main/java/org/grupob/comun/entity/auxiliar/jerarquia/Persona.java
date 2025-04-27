package org.grupob.comun.entity.auxiliar.jerarquia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.DireccionPostal;
import org.grupob.comun.entity.maestras.Genero;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data

@MappedSuperclass
public class Persona {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;

    private String apellido;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_persona_genero_id"))
    private Genero genero;

    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private Integer edad;
    private String paisNacimiento;

    private String tipoDocumento;
    private String numDocumento;

    //probablemente esto cambie
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipo", column = @Column(name = "tipo_via")),
    })
    private DireccionPostal direccion;

}
