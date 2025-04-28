package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_etiqueta_nombre", columnNames = {"nombre", "creador_id"}) // Una etiqueta es única por nombre y creador (jefe)
})
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false, foreignKey = @ForeignKey(name = "FK_etiqueta_creador"))
    private Empleado creador; // El jefe que creó/usó esta etiqueta por primera vez

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToMany(mappedBy = "etiquetas")
    @EqualsAndHashCode.Exclude // Evitar recursión en hashCode/equals
    private Set<Empleado> empleados = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    public Etiqueta(String nombre, Empleado creador) {
        this.nombre = nombre;
        this.creador = creador;
    }



    // Evitar recursión en toString
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ")";
    }
    @Override
    public boolean equals(Object o) {
        // 1. Comparación de identidad rápida
        if (this == o) return true;
        // 2. Verificar nulidad y tipo (usando getClass() para proxies)
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Castear el objeto
        Etiqueta etiqueta = (Etiqueta) o;
        // 4. Comparar SÓLO por el ID. Si el ID es null (entidad nueva no persistida),
        //    dos instancias nunca son iguales a menos que sean la misma instancia (chequeado en paso 1).
        return id != null && Objects.equals(id, etiqueta.id);
    }

    @Override
    public int hashCode() {
        // 5. Calcular hashCode SÓLO basado en el ID.
        //    Si el ID es null, devuelve un hash consistente (ej. de la clase).
        //    Usar Objects.hash maneja el caso null. O devolver una constante.
        // return Objects.hash(id);
        // Alternativa común para entidades JPA:
        return getClass().hashCode(); // Hash constante si el ID es null (entidad nueva)
        // Si quieres basarlo en ID sólo si no es null:
        // return id != null ? id.hashCode() : getClass().hashCode();
    }

}