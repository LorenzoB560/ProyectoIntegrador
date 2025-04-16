package org.grupob.adminapp.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.jerarquia.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto2 extends Producto {

    private String marca;


}
