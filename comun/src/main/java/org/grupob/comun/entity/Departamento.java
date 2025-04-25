package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_departamento_codigo", columnNames = "codigo"),
//        @UniqueConstraint(name = "UQ_departamento_id_jefe", columnNames = "id_jefe")
})
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;
    private String codigo;

//    @Column(name = "presupuesto_anual")
//    private BigDecimal presupuesto;

    private String localidad;

//    @OneToOne
//    @JoinColumn(name = "id_jefe", foreignKey = @ForeignKey(name = "FK_empelado_departamento_id"))
//    private Empleado jefe;

    public Departamento(String nombre, String codigo) {
//        id = UUID.randomUUID().to;
        this.nombre = nombre;
        this.codigo = codigo;
    }


    @OneToMany(mappedBy = "departamento") //como se llama el atributo que se relaciona con la otra tabla
    private Set<Empleado> listaEmpleados;

    //CLASE
//    private LocalDateTime fechaAltaBaseDatos;
}
