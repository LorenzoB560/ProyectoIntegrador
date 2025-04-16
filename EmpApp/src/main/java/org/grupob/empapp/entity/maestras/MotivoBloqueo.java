package org.grupob.empapp.entity.maestras;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MotivoBloqueo {
    @Id
    private Long id;
    private String motivo;

    // ajustable segun pruebas, en minutos
    private Integer minutos;

    public MotivoBloqueo(String motivo, Integer minutos) {
        setMotivo(motivo);
        setMinutos(minutos);
    }
}
