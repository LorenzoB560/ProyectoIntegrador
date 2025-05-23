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

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("ELECTRONICO")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Electronico extends Producto {

    private String modelo;
    private String garantia;
    private BigDecimal pulgadas;
    private Dimension dimension;
    @JoinColumn(name="capacidad_bateria")
    private Integer capacidadBateria;
    @JoinColumn(name="almacenamiento_interno")
    private Integer almacenamientoInterno;
    private Integer ram;



}
