package org.grupob.adminapp.service;


import org.grupob.comun.entity.Empleado;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.adminapp.dto.EmpleadoSearchDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con empleados
 */
public interface EmpleadoService {

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
     List<EmpleadoDTO> buscarEmpleados(EmpleadoSearchDTO searchParams);

     List<EmpleadoDTO> buscarEmpleadosAvanzado(
             String nombre,
             String departamento,
             String trabajo,
             LocalDate contratadosAntesDe,
             BigDecimal salarioMinimo);

     List<EmpleadoDTO> buscarEmpleadosPorDepartamento(String departamento);

     List<EmpleadoDTO> buscarEmpleadosPorComentario(String Comentario);

     /**
      * Método para búsqueda paginada y ordenada
      */
     Page<EmpleadoDTO> buscarEmpleadosPaginados(
             String nombre,
             String departamento,
             String comentario,
             LocalDate contratadosAntesDe,
             BigDecimal salarioMinimo,
             int page,
             int size,
             String sortBy,
             String sortDir);


     // Métodos de gestión de jefes
     EmpleadoDTO asignarJefe(String empleadoId, String jefeId);
     EmpleadoDTO quitarJefe(String empleadoId);
     List<EmpleadoDTO> listarSubordinados(String jefeId);

     // Métodos de gestión de etiquetas
//     EmpleadoDTO asignarEtiqueta(String empleadoId, String etiquetaId);
//     EmpleadoDTO quitarEtiqueta(String empleadoId, String etiquetaId);
//     List<EmpleadoDTO> buscarPorEtiqueta(String etiquetaId);
}
