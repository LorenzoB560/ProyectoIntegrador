package org.grupob.empapp.dto; // o donde tengas tus DTOs de EmpApp

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboracionEstablecidaDTO {
    private UUID idColaboracion;
    private String otroEmpleadoNombreCompleto;
    private UUID otroEmpleadoId;
    private LocalDateTime fechaCreacionColaboracion; // Fecha de la primera vez que se estableció
    private List<PeriodoDTO> periodos;
    private boolean actualmenteActiva; // Para saber si hay un periodo activo ahora mismo

    // Podrías añadir un constructor si es útil
}
