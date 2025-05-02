package org.grupob.comun.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("ROPA")
public class Ropa extends Producto {

    private String talla;
    private String color;
    private String material;

}
