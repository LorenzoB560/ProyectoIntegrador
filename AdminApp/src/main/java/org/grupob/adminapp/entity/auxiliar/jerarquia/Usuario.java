package org.grupob.adminapp.entity.auxiliar.jerarquia;

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
    private String correo;

    @Column(nullable = false)
    private String clave;


    public Usuario(String correo, String clave) {
        setCorreo(correo);
        setClave(clave);
    }
}

