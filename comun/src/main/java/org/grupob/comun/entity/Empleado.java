package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode; // Asegúrate de tener esta importación
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.auxiliar.CuentaBancaria;
import org.grupob.comun.entity.auxiliar.Periodo;
import org.grupob.comun.entity.auxiliar.TarjetaCredito;
import org.grupob.comun.entity.auxiliar.jerarquia.Persona;
import org.grupob.comun.entity.maestras.TipoTarjetaCredito;

import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"jefe", "listaNominas", "especialidades", "etiquetas"}) // Excluir colecciones y relaciones recursivas
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "PK_empleado", columnNames = "id"),
        @UniqueConstraint(name = "UQ_empleado_id_usuario", columnNames = "id_usuario"),
})
@SecondaryTable(name = "informacion_economica", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class Empleado extends Persona {

    private String comentarios;

    @ManyToMany
    @JoinTable(
            name = "empleado_especialidad",
            joinColumns = @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_empleado_especialidad_empleado_id")),
            inverseJoinColumns = @JoinColumn(name = "id_especialidad", foreignKey = @ForeignKey(name = "FK_empleado_especialidad_especialidad_id"))
    )
    private Set<Especialidad> especialidades = new HashSet<>(); // Inicializar colecciones

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching para jefe
    @JoinColumn(name = "id_jefe", foreignKey = @ForeignKey(name = "FK_empleado_empleado_id"))
    private Empleado jefe;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fechaInicio", column = @Column(name = "fecha_contratacion")),
            @AttributeOverride(name = "fechaFin", column = @Column(name = "fecha_cese")),
    })
    private Periodo periodo;

    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching
    @JoinColumn(name = "id_departamento", foreignKey = @ForeignKey(name = "FK_departamento_empleado_id"))
    private Departamento departamento;

    @OneToOne(fetch = FetchType.LAZY) // Lazy fetching
    @JoinColumn(name = "id_usuario",
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

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching
    @JoinColumn(name = "id_entidad_bancaria", foreignKey = @ForeignKey(name = "FK_empleado_entidad_bancaria_id"), table = "informacion_economica")
    private EntidadBancaria entidadBancaria;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching
    @JoinColumn(name = "id_tipo_tarjeta", foreignKey = @ForeignKey(name = "FK_empleado_tipo_tarjeta_id"), table = "informacion_economica")
    private TipoTarjetaCredito tipoTarjetaCredito;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Nomina> listaNominas = new HashSet<>(); // Inicializar colecciones

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numero", column = @Column(name = "numero", table = "informacion_economica")),
            @AttributeOverride(name = "mesCaducidad", column = @Column(name = "mes_caducidad", table = "informacion_economica")),
            @AttributeOverride(name = "anioCaducidad", column = @Column(name = "anio_caducidad", table = "informacion_economica")),
            @AttributeOverride(name = "CVC", column = @Column(name = "CVC", table = "informacion_economica"))
    })
    private TarjetaCredito tarjetaCredito;

    @Lob
    @Basic(fetch = FetchType.LAZY) // Carga perezosa para la foto
    @Column(columnDefinition = "LONGBLOB")
    private byte[] foto;

    // --- NUEVA RELACIÓN CON ETIQUETA ---
    @ManyToMany(fetch = FetchType.LAZY) // Carga perezosa
    @JoinTable(
            name = "empleado_etiqueta",
            joinColumns = @JoinColumn(name = "id_empleado", foreignKey = @ForeignKey(name = "FK_empleado_etiqueta_empleado_id")),
            inverseJoinColumns = @JoinColumn(name = "id_etiqueta", foreignKey = @ForeignKey(name = "FK_empleado_etiqueta_etiqueta_id"))
    )
    private Set<Etiqueta> etiquetas = new HashSet<>(); // Inicializar colección

    // --- MÉTODOS HELPER PARA ETIQUETAS ---
    public void addEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.add(etiqueta);
        etiqueta.getEmpleados().add(this);
    }

    public void removeEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.remove(etiqueta);
        etiqueta.getEmpleados().remove(this);
    }

    // --- Evitar recursión en toString ---
    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + getId() + // Usar getter de Persona
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", documento='" + getDocumento() + '\'' +
                ", fechaNacimiento=" + getFechaNacimiento() +
                ", genero=" + (getGenero() != null ? getGenero().getGenero() : null) +
                ", activo=" + activo +
                ", departamentoId=" + (departamento != null ? departamento.getId() : null) +
                ", jefeId=" + (jefe != null ? jefe.getId() : null) +
                ", usuarioId=" + (usuario != null ? usuario.getId() : null) +
                ", especialidadesCount=" + (especialidades != null ? especialidades.size() : 0) +
                ", etiquetasCount=" + (etiquetas != null ? etiquetas.size() : 0) +
                '}';
    }
}