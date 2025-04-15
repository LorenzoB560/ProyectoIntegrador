package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.Empleado;
import org.grupob.empapp.entity.maestras.MotivoBloqueo;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor

@MappedSuperclass

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(nullable = false)
    private String clave;



    public Usuario(String correo, String clave) {
        setCorreo(correo);
        setClave(clave);
    }
}

