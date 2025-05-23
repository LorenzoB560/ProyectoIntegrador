package org.grupob.comun.entity.auxiliar.jerarquia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupob.comun.entity.auxiliar.DireccionPostal;
import org.grupob.comun.entity.maestras.Genero;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class Persona {

    @Id
    protected UUID id;

    private String nombre;
    private String apellido;
    private String tipoDocumento;
    private String numDocumento;

    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String paisNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_persona_genero_id"))
    private Genero genero;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipoVia", column = @Column(name = "tipo_via")),
            @AttributeOverride(name = "via", column = @Column(name = "via")),
            @AttributeOverride(name = "numero", column = @Column(name = "numero")),
            @AttributeOverride(name = "portal", column = @Column(name = "portal")),
            @AttributeOverride(name = "planta", column = @Column(name = "planta")),
            @AttributeOverride(name = "puerta", column = @Column(name = "puerta")),
            @AttributeOverride(name = "localidad", column = @Column(name = "localidad")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "codigo_postal")),
            @AttributeOverride(name = "region", column = @Column(name = "region")),
            @AttributeOverride(name = "pais", column = @Column(name = "pais"))
    })
    private DireccionPostal direccion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona persona)) return false; // instanceof mejor
        return id != null && Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ")";
    }
}