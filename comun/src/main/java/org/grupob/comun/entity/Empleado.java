
package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.*;
import org.grupob.comun.entity.auxiliar.CuentaBancaria;
import org.grupob.comun.entity.auxiliar.Periodo;
import org.grupob.comun.entity.auxiliar.TarjetaCredito;
import org.grupob.comun.entity.auxiliar.jerarquia.Persona;
import org.grupob.comun.entity.maestras.TipoTarjetaCredito;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "PK_empleado", columnNames = "id"),
        @UniqueConstraint(name = "UQ_empleado_id_usuario", columnNames = "id_usuario"),
//        @UniqueConstraint(name = "UQ_empleado_dni", columnNames = "dni")
})
@SecondaryTable(name = "informacion_economica", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class Empleado extends Persona {

//    private String dni;

    private String comentarios;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "empleado_especialidad",
            joinColumns = @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_empleado_especialidad_empleado_id")),
            inverseJoinColumns = @JoinColumn(name = "id_especialidad", foreignKey = @ForeignKey(name = "FK_empleado_especialidad_especialidad_id"))
    )
    private Set<Especialidad> especialidades;

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
    @OneToOne//(optional = false)
    @JoinColumn(name = "id_usuario", //nullable = false,
            foreignKey = @ForeignKey(name = "FK_empleado_usuario_id"))
    private UsuarioEmpleado usuario;

    @Column(table = "informacion_economica")
    private BigDecimal salario;
    @Column(table = "informacion_economica")
    private BigDecimal comision;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "IBAN", column = @Column(name = "IBAN", table = "informacion_economica"))
    })
    private CuentaBancaria cuentaCorriente;

    @ManyToOne
    @JoinColumn(name = "id_entidad_bancaria", foreignKey = @ForeignKey(name = "FK_empleado_entidad_bancaria_id"), table = "informacion_economica")
    private EntidadBancaria entidadBancaria;

    // --- NUEVA RELACIÓN CON ETIQUETA ---
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "empleado_etiqueta",
            joinColumns = @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_empleado_etiqueta_empleado_id")),
            inverseJoinColumns = @JoinColumn(name = "id_etiqueta", foreignKey = @ForeignKey(name = "FK_empleado_etiqueta_etiqueta_id"))
    )
    private Set<Etiqueta> etiquetas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_tipo_tarjeta", foreignKey = @ForeignKey(name = "FK_empleado_tipo_tarjeta_id"), table = "informacion_economica")
    private TipoTarjetaCredito tipoTarjetaCredito;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Nomina> listaNominas;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numero", column = @Column(name = "numero", table = "informacion_economica")),
            @AttributeOverride(name = "mesCaducidad", column = @Column(name = "mes_caducidad", table = "informacion_economica")),
            @AttributeOverride(name = "anioCaducidad", column = @Column(name = "anio_caducidad", table = "informacion_economica")),
            @AttributeOverride(name = "CVC", column = @Column(name = "CVC", table = "informacion_economica"))
    })
    private TarjetaCredito tarjetaCredito;

    @Override
    public boolean equals(Object o) {
        // 1. Comparación de identidad rápida
        if (this == o) return true;
        // 2. Verificar nulidad y tipo (usando getClass() para proxies)
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Castear el objeto
        Empleado empleado = (Empleado) o;
        // 4. Comparar SÓLO por el ID. Si el ID es null (entidad nueva no persistida),
        //    dos instancias nunca son iguales a menos que sean la misma instancia (chequeado en paso 1).
        return id != null && Objects.equals(id, empleado.id);
    }

    @Override
    public int hashCode() {
        // 5. Calcular hashCode SÓLO basado en el ID.
        //    Si el ID es null, devuelve un hash consistente (ej. de la clase).
        //    Usar Objects.hash maneja el caso null. O devolver una constante.
        // return Objects.hash(id);
        // Alternativa común para entidades JPA:
        return getClass().hashCode(); // Hash constante si el ID es null (entidad nueva)
        // Si quieres basarlo en ID sólo si no es null:
        // return id != null ? id.hashCode() : getClass().hashCode();
    }


    @Lob
    @Column(columnDefinition = "LONGBLOB") //nos aseguramos quepueda almacenar un tamaño grande de archivo
    private byte[] foto; // Para almacenar la imagen en la base de datos

//    @Column(name = "fecha_eliminacion")
//    private LocalDate fechaEliminacion;
//
//    @Column(name = "fecha_insercion")
//    private LocalDate fechaInsercion;
@Override
public String toString() {
    // Incluir SOLO campos simples o IDs de relaciones
    return getClass().getSimpleName() + "(" +
            "id=" + getId() + // Usar getter de Persona si ID está allí
            ", nombre='" + getNombre() + '\'' + // Asume getter en Persona o Empleado
            ", apellido='" + getApellido() + '\'' + // Asume getter en Persona o Empleado
            ", activo=" + activo +
            ")";
}

}