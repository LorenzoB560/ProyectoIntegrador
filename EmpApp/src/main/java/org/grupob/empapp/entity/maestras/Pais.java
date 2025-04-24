package org.grupob.empapp.entity.maestras;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Pais {
    @Id
    private Long id;
    private String pais;
    private String prefijo;
}
