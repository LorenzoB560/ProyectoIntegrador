package org.grupob.proyectointegrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.proyectointegrador.entity.auxiliar.CuentaBancaria;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "informacion_economica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionEconomica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "salario", nullable = false)
    private BigDecimal salario;

    @Column(name = "comision")
    private BigDecimal comision;

    // Clave ajena: empleado (relación 1:1)
    @OneToOne
    @JoinColumn(name = "id_empleado", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "FK_informacion_economica_empleado_id_empleado"))
    private Empleado empleado;


    @Embedded
    @Column(name = "cuenta_corriente")
    private CuentaBancaria cuentaCorriente;

    @OneToOne(mappedBy = "informacionEconomica", cascade = CascadeType.ALL, orphanRemoval = true)
    private LineaNomina lineaNomina;


}
