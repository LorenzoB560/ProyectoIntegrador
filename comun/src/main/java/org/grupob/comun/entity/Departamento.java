package org.grupob.comun.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
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


    private String localidad;

    public Departamento(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }


    @OneToMany(mappedBy = "departamento") //como se llama el atributo que se relaciona con la otra tabla
    private Set<Empleado> listaEmpleados;

    @Override
    public boolean equals(Object o) {
        // 1. Comparación de identidad rápida
        if (this == o) return true;
        // 2. Verificar nulidad y tipo (usando getClass() para proxies)
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Castear el objeto
        Departamento departamento = (Departamento) o;
        // 4. Comparar SÓLO por el ID. Si el ID es null (entidad nueva no persistida),
        //    dos instancias nunca son iguales a menos que sean la misma instancia (chequeado en paso 1).
        return id != null && Objects.equals(id, departamento.id);
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

}
