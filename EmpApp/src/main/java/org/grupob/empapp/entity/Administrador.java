package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.Usuario;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Administrador extends Usuario {

    public Administrador(String email, String clave) {
        super(email, clave);
    }
}
