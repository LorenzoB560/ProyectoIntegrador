package org.grupob.empapp.dto.auxiliar;

import lombok.Data;

@Data
public class GeneroDTO {
    private Long id;
    private String genero;

    public GeneroDTO(Long id, String genero) {
    }
}
