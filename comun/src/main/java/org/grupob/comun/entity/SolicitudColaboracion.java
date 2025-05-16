package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.maestras.Estado;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "solicitud_colaboracion")
public class SolicitudColaboracion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante")
    private Empleado solicitante;

    @ManyToOne
    @JoinColumn(name = "id_receptor")
    private Empleado receptor;

    @Column(name="fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name="fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

    @Column(name="fecha_rechazo")
    private LocalDateTime fechaRechazo;

    @Column(name="fecha_desbloqueo")
    private LocalDateTime fecha_desbloqueo;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

}
