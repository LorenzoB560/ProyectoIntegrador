package org.grupob.adminapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.grupob.adminapp.entity.auxiliar.jerarquia.Usuario;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_administrador_correo", columnNames = "correo")
})
public class Administrador extends Usuario {

    public Administrador(String email, String clave) {
        super(email, clave);
    }
}
