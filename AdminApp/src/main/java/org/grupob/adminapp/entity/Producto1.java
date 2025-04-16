package org.grupob.adminapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.jerarquia.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto1 extends Producto {

    private String autor;
    private String editorial;

}
