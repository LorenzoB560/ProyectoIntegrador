package org.grupob.empapp.service;

import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.comun.entity.Etiqueta; // Necesitamos la entidad aquí

import java.util.List;
import java.util.UUID;

public interface EtiquetaService {

    /**
     * Lista todas las etiquetas creadas por un jefe específico.
     * @param jefeId ID del jefe (como String).
     * @return Lista de EtiquetaDTO.
     */
    List<EtiquetaDTO> listarEtiquetasPorJefe(String jefeId);

    /**
     * Lista todas las etiquetas globales (si aplica).
     * @return Lista de EtiquetaDTO.
     */
    List<EtiquetaDTO> listarTodasEtiquetasGlobales();

    /**
     * Asigna un conjunto de etiquetas a un conjunto de empleados (subordinados del jefe).
     * @param jefeId ID del jefe que realiza la acción (como String).
     * @param empleadoIds Lista de IDs de empleados (como String).
     * @param etiquetaIds Lista de IDs de etiquetas (como String).
     */
    void asignarEtiquetasMasivo(String jefeId, List<String> empleadoIds, List<String> etiquetaIds);

    /**
     * Asigna una etiqueta (existente o nueva) a un empleado específico.
     * @param empleadoId ID del empleado (como String).
     * @param nombreEtiqueta Nombre de la etiqueta.
     * @param jefeId ID del jefe que realiza la acción (como String).
     * @return EmpleadoDTO actualizado.
     */
    EmpleadoDTO asignarEtiquetaSimple(String empleadoId, String nombreEtiqueta, String jefeId);

    /**
     * Elimina una etiqueta específica de un empleado.
     * @param empleadoId ID del empleado (como String).
     * @param etiquetaId ID de la etiqueta (como String).
     * @param jefeId ID del jefe (para validación, como String).
     * @return EmpleadoDTO actualizado.
     */
    EmpleadoDTO eliminarEtiquetaDeEmpleado(String empleadoId, String etiquetaId, String jefeId);

    /**
     * Busca o crea una etiqueta para un jefe específico. (Puede ser interno o público)
     * @param nombreEtiqueta El nombre de la etiqueta.
     * @param jefeId El ID del jefe que la crea/usa (como UUID).
     * @return La Etiqueta encontrada o recién creada.
     */
    Etiqueta buscarOCrearEtiqueta(String nombreEtiqueta, UUID jefeId);
}