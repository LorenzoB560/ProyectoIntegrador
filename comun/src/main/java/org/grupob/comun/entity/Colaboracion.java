package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.*;
import org.grupob.comun.entity.auxiliar.Periodo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Colaboracion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_emisor")
    private Empleado emisor;

    @ManyToOne
    @JoinColumn(name = "id_receptor")
    private Empleado receptor;

    @ElementCollection
    @CollectionTable(name = "periodo_colaboracion")
    private List<Periodo> periodos;

    @OneToMany(mappedBy = "colaboracion", cascade = CascadeType.ALL)
    private List<Mensaje> mensajes;

    @Column(name="fecha_creacion")
    private LocalDateTime fechaCreacion;

    // Método para obtener el periodo activo
    public Optional<Periodo> getPeriodoActivo() {
        return periodos.stream()
                .filter(p -> p.getFechaFin() == null)
                .findFirst();
    }
}
