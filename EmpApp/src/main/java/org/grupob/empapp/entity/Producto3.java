package org.grupob.empapp.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto3 extends Producto {

    private String material;

}
