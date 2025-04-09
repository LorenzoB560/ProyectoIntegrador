package org.grupob.proyectointegrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.proyectointegrador.entity.auxiliar.Genero;

import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass
@DiscriminatorColumn(name = "tipo_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;



    @Column(name = "Precio", nullable = false)
    private Double precio;


    @Column(name = "Descripcion", nullable = false)
    private String Descripcion;
}
