package org.grupob.comun.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.Dimension;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("MUEBLE")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Mueble extends Producto {

    private Dimension dimension;
    private String material;
    @JoinColumn(name="fecha_fabricacion")
    private LocalDate fechaFabricacion;

}
