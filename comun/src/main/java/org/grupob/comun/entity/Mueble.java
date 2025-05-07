package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.Dimension;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;
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
    @ElementCollection
    @CollectionTable(name = "mueble_colores", joinColumns = @JoinColumn(name = "producto_id"))
    private List<String> colores;

}
