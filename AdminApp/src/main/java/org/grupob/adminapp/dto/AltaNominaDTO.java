package org.grupob.adminapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaNominaDTO {

    private UUID id;

    private String mes;
    private String anio;

    private UUID idEmpleado;
}
