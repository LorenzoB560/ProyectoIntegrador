package org.grupob.empapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.empapp.entity.auxiliar.CuentaBancaria;
import org.grupob.empapp.entity.auxiliar.Periodo;
import org.grupob.empapp.entity.auxiliar.Persona;
import org.grupob.empapp.entity.auxiliar.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "PK_empleado", columnNames = "id"),
        @UniqueConstraint(name = "UQ_empleado_id_usuario", columnNames = "id_usuario"),
//        @UniqueConstraint(name = "UQ_empleado_dni", columnNames = "dni")
})
@SecondaryTable(name = "informacion_economica", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"),
        foreignKey = @ForeignKey(name = "FK_empelado_informacion_economica_id"))
public class Empleado extends Persona {

//    private String dni;

    @ManyToOne
    @JoinColumn(name = "id_jefe", foreignKey = @ForeignKey(name = "FK_empleado_empleado_id"))
    private Empleado jefe;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fechaInicio", column = @Column(name = "fecha_contratacion")),
            @AttributeOverride(name = "fechaFin", column = @Column(name = "fecha_cese")),
    })
    private Periodo periodo;

    private boolean activo;

    //@DondeEstoy-DondeVoy
    //Many empleados pertenecen a One Departamento
    @ManyToOne
    @JoinColumn(name = "id_departamento", foreignKey = @ForeignKey(name = "FK_departamento_empleado_id"))
    private Departamento departamento;


    //LADO PROPIETARIO DE LA RELACION (es mas frecuente que se consulten datos desde aqui a la otra tabla)
    @OneToOne
    @JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "FK_empleado_usuario_empleado_id"))
    private UsuarioEmpleado usuario;

    @Column(table = "informacion_economica")
    private BigDecimal salario;
    @Column(table = "informacion_economica")
    private BigDecimal comision;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "numero_cuenta", column = @Column(table = "informacion_economica", name = "numero_cuenta")),
//            @AttributeOverride(name = "entidad", column = @Column(table = "informacion_economica", name = "entidad")),
//    })
//    private CuentaBancaria cuentaCorriente;


    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Nomina> listaNominas;

    @Lob
    @Column(columnDefinition = "LONGBLOB") //nos aseguramos quepueda almacenar un tamaño grande de archivo
    private byte[] foto; // Para almacenar la imagen en la base de datos

//    @Column(name = "fecha_eliminacion")
//    private LocalDate fechaEliminacion;
//
//    @Column(name = "fecha_insercion")
//    private LocalDate fechaInsercion;

}
