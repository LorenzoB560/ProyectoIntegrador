package org.grupob.adminapp.entity.maestras;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor @NoArgsConstructor @Data
@Entity
public class Genero {
    @Id
    private Long id;
    private String genero;
}

