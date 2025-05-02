package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("LIBRO")
public class Libro extends Producto{

    private String autor;
    private String editorial;
    @Column(name = "num_paginas")
    private Integer numPaginas;

}
