package org.grupob.empapp.entity.auxiliar.jerarquia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor

@MappedSuperclass
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String clave;

    @Column(name = "num_accesos")
    private Integer numeroAccesos;


    public Usuario(String usuario, String clave) {
        setUsuario(usuario);
        setClave(clave);
        setNumeroAccesos(0);
    }
}

