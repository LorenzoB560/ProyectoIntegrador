package org.grupob.adminapp.service;


import lombok.RequiredArgsConstructor;
import org.grupob.comun.entity.Departamento;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.adminapp.converter.DepartamentoConverter;
import org.grupob.comun.dto.DepartamentoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImp implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoConverter departamentoConverter;

    public List<DepartamentoDTO> devuelveTodosDepartamentos(){
        List<Departamento> listadepartamentos = departamentoRepository.findAll();
        return listadepartamentos
            .stream()
            .map(departamentoConverter::convertToDto)
            .toList();
    }


    public DepartamentoDTO devuelveDepartamento(String id) {
        Departamento departamento = departamentoRepository.getReferenceById(UUID.fromString(id));
        return departamentoConverter.convertToDto(departamento);
    }

    @Override
    public void  eliminaDepartamentoPorId(String id) {
        UUID uuid= UUID.fromString(id);
        if(departamentoRepository.existsById(uuid)){
            departamentoRepository.deleteById(uuid);
        }
        throw new DepartamentoNoEncontradoException("El departamento no existe");
    }

    public Departamento devuleveDepartartamentoPorNombre(String dname){
       Optional<Departamento> deptOpc= departamentoRepository.findDepartamentoByNombre(dname);
       if(deptOpc.isPresent()){
        System.err.println(deptOpc.get());
        return deptOpc.get();
       }else {
           throw new DepartamentoNoEncontradoException("El departamento no existe");
       }
     }

    public Departamento guardarDepartamento(Departamento departamento){
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento modificarDepartamento(String id, Departamento departamento) {
        UUID uuid= UUID.fromString(id);
        if(departamentoRepository.existsById(uuid)){
            departamento.setId(uuid);
            
            return departamentoRepository.save(departamento);
        }
        throw new DepartamentoNoEncontradoException("El departamento no existe");
    }

}
