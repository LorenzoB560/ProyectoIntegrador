package org.grupob.empapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoSimpleDTO {
    private UUID id;
    private String nombreCompleto;
}
