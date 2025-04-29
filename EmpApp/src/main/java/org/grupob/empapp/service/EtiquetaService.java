package org.grupob.empapp.service;

import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.comun.entity.Etiqueta; // Necesitamos la entidad aquí

import java.util.List;
import java.util.UUID;

public interface EtiquetaService {
    List<EtiquetaDTO> listarEtiquetasPorJefe(String jefeId);

    List<EtiquetaDTO> listarTodasEtiquetasGlobales(); // Podría seguir siendo útil

    /**
     * Asigna una etiqueta existente a un empleado específico.
     * @param empleadoId ID del empleado (como String).
     * @param etiquetaId ID de la etiqueta a asignar (como String).
     * @param jefeId ID del jefe que realiza la acción (para validación, como String).
     * @return EmpleadoDTO actualizado.
     */
    EmpleadoDTO asignarEtiquetaExistente(String empleadoId, String etiquetaId, String jefeId); // Cambiado de asignarEtiquetaSimple

    /**
     * Elimina una etiqueta específica de un empleado.
     * @param empleadoId ID del empleado (como String).
     * @param etiquetaId ID de la etiqueta (como String).
     * @param jefeId ID del jefe (para validación, como String).
     * @return EmpleadoDTO actualizado.
     */
    EmpleadoDTO eliminarEtiquetaDeEmpleado(String empleadoId, String etiquetaId, String jefeId);

    /**
     * Busca todos los empleados que tienen asociada una etiqueta específica.
     * @param etiquetaId ID de la etiqueta (como String).
     * @return Lista de EmpleadoDTO.
     */
    List<EmpleadoDTO> buscarEmpleadosPorEtiqueta(String etiquetaId);


    // Mantenemos buscarOCrearEtiqueta si aún se necesita en algún punto (quizás para una UI diferente)
    Etiqueta buscarOCrearEtiqueta(String nombreEtiqueta, UUID jefeId);
}