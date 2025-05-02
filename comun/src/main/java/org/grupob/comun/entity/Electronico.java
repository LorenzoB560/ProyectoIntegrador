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
@DiscriminatorValue("ELECTRONICO")
public class Electronico extends Producto {

    private String marca;
    private String modelo;
    private String garantia;


}
