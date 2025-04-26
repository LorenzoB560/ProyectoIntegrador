package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EntidadBancaria {

    @Id
    private UUID id;

    private String codigo;
    private String nombre;

    @OneToMany(mappedBy = "entidadBancaria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Empleado> empleados;
}
