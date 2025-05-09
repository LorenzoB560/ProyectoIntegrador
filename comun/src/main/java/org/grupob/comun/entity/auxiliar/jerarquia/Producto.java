package org.grupob.comun.entity.auxiliar.jerarquia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.maestras.Categoria;
import org.grupob.comun.entity.maestras.Proveedor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_producto")
public abstract class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;

    private String marca;

    @ManyToMany
    @JoinTable(name="producto_categoria",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private Set<Categoria> categoria;

    private Boolean segundaMano;

    private Integer unidades;

    @JoinColumn(name="fecha_fabricacion")
    private LocalDate fechaFabricacion;

    @ManyToOne
    private Proveedor proveedor;

    @JoinTable(name="fecha_alta")
    private LocalDate fechaAlta;

    private Integer valoracion;



}

