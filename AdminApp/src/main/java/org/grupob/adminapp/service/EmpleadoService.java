package org.grupob.adminapp.service;


import org.grupob.comun.entity.Empleado;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.comun.dto.EmpleadoSearchDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con empleados
 */
public interface EmpleadoService {

     /**
      * Tarea programada para desbloquear empleados que están marcados como bloqueados.
      * Resetea el estado de bloqueo, intentos fallidos, fecha de bloqueo y motivo.
      */
     void desbloquearEmpleadosBloqueadosAutomaticamente();

     /**
      * Obtiene la lista de nombres de empleados que han sido desbloqueados recientemente.
      * La lista se limpia después de ser consultada.
      * @return Lista de nombres completos de empleados.
      */
     List<String> getNombresEmpleadosDesbloqueadosRecientemente();

     /**
      * Limpia la lista interna de empleados desbloqueados recientemente.
      */
     void clearNombresEmpleadosDesbloqueadosRecientemente();

     /**
      * Métodos CRUD básicos
      */
     List<EmpleadoDTO> devuelveTodosEmpleados();

     EmpleadoDTO devuelveEmpleado(String id);

     void eliminaEmpleadoPorId(String id);

     Empleado devuleveEmpleadoPorNombre(String ename);

     Empleado guardarEmpleado(Empleado empleado);

     Empleado modificarEmpleado(String id, Empleado empleado);

     /**
      * Métodos de búsqueda parametrizada
      */
//     List<EmpleadoDTO> buscarEmpleados(EmpleadoSearchDTO searchParams);

     List<EmpleadoDTO> buscarEmpleadosAvanzado(
             EmpleadoSearchDTO searchParams);

     List<EmpleadoDTO> buscarEmpleadosPorDepartamento(String departamento);

     List<EmpleadoDTO> buscarEmpleadosPorComentario(String Comentario);

     EmpleadoDTO desactivarEmpleado(String id);
     EmpleadoDTO activarEmpleado(String id);

     /**
      * Método para búsqueda paginada y ordenada
      */
     Page<EmpleadoDTO> buscarEmpleadosPaginados(
             EmpleadoSearchDTO searchParams,
             int page,
             int size,
             String sortBy,
             String sortDir);


     List<EmpleadoDTO> devuelveTodosEmpleadosInactivos();

     List<EmpleadoDTO> devuelveTodosEmpleadosActivos();
     // Métodos de gestión de jefes
     EmpleadoDTO asignarJefe(String empleadoId, String jefeId);
     EmpleadoDTO quitarJefe(String empleadoId);
     List<EmpleadoDTO> listarSubordinados(String jefeId);

     // Métodos de gestión de etiquetas
//     EmpleadoDTO asignarEtiqueta(String empleadoId, String etiquetaId);
//     EmpleadoDTO quitarEtiqueta(String empleadoId, String etiquetaId);
//     List<EmpleadoDTO> buscarPorEtiqueta(String etiquetaId);
}
