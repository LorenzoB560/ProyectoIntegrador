package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto1 extends Producto {

    private String autor;
    private String editorial;

}
