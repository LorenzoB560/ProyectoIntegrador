package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.comun.entity.maestras.Talla;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("ROPA")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Ropa extends Producto {

    @ManyToMany
    @JoinTable(name="ropa_talla",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_talla")
    )
    private List<Talla> tallas;
    private String material;


}
