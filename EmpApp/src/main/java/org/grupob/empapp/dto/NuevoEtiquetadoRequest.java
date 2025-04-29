package org.grupob.empapp.dto; // O el paquete que uses para DTOs/Requests

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor@NoArgsConstructor
@Data
public class NuevoEtiquetadoRequest {

    private List<String> empleadoIds; // Lista de UUIDs de los empleados seleccionados
    private String nombreEtiqueta;    // El nombre de la etiqueta (nueva o existente)

}
