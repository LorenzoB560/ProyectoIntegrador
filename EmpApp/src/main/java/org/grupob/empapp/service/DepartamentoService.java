package org.grupob.empapp.service;


import org.grupob.comun.dto.DepartamentoDTO;
import org.grupob.comun.entity.Departamento;


import java.util.List;


public interface  DepartamentoService {

     List<DepartamentoDTO> devuelveTodosDepartamentos();
     DepartamentoDTO devuelveDepartamento( String id);
     void eliminaDepartamentoPorId( String id);
     Departamento devuleveDepartartamentoPorNombre(String dname);
     Departamento guardarDepartamento(Departamento departamento);

     Departamento modificarDepartamento(String id, Departamento departamento);
}
