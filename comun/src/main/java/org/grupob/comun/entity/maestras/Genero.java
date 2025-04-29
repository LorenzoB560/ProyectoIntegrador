package org.grupob.comun.entity.maestras;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@AllArgsConstructor @NoArgsConstructor @Data
@Entity
public class Genero {
    @Id
    private Long id;
    private String genero;
}

