package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Colaboracion colaboracion;

    @ManyToOne
    private Empleado remitente;

    @ManyToOne
    private Empleado destinatario;

    private String contenido;

    @Column(name="fecha_envio")
    private LocalDateTime fechaEnvio;

    @ManyToOne
    private Mensaje mensajePadre;
}
