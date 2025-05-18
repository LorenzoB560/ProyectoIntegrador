package org.grupob.comun.entity.maestras;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String motivo;

    // ajustable segun pruebas, en minutos
    private Integer minutos;

    public MotivoBloqueo(String motivo, Integer minutos) {
        setMotivo(motivo);
        setMinutos(minutos);
    }
}
