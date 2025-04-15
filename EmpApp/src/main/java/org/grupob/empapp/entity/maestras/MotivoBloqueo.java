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
    private Integer tiempo;

    public MotivoBloqueo(String motivo, Integer minutosBloqueo) {
        setMotivo(motivo);
        setTiempo(minutosBloqueo);
    }
}
