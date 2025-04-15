package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")
@DiscriminatorValue("PRODUCTO")
public class Producto1 extends Producto {

    private Integer edad;

}
