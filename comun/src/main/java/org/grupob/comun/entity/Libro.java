package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("LIBRO")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Libro extends Producto{

    private String titulo;
    private String autor;
    private String editorial;
    @Column(name = "num_paginas")
    private Integer numPaginas;

}
