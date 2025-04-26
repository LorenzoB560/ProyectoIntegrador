package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_empleado", columnNames = "codigo"),
})
public class Especialidad {

    @Id
    private UUID id;
    private String codigo;
    private String nombre;

}
