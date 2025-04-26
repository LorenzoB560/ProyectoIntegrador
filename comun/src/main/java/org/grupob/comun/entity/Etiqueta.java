package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_etiqueta_nombre", columnNames = {"nombre", "creador_id"}) // Una etiqueta es única por nombre y creador (jefe)
})
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
        return "Etiqueta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", creadorId=" + (creador != null ? creador.getId() : null) + // Solo ID para evitar ciclo
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}