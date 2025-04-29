package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.grupob.comun.entity.auxiliar.jerarquia.Usuario;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_administrador_usuario", columnNames = "usuario")
})
public class Administrador extends Usuario {

    public Administrador(String usuario, String clave) {
        super(usuario, clave);
    }
}
