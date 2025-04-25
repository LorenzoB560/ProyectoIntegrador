package org.grupob.empapp.service;


import org.grupob.empapp.converter.DepartamentoConverter;
import org.grupob.empapp.dto.DepartamentoDTO;
import org.grupob.comun.entity.Departamento;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartamentoServiceImp implements DepartamentoService {

//    private DepartamentoRepository departamentoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoConverter departamentoConverter;

    public DepartamentoServiceImp(DepartamentoRepository departamentoRepository, DepartamentoConverter departamentoConverter) {
        this.departamentoRepository = departamentoRepository;
//        this.departamentoRepository = departamentoRepository;
        this.departamentoConverter = departamentoConverter;

    }
    public List<DepartamentoDTO> devuelveTodosDepartamentos(){
        List<Departamento> listadepartamentos = departamentoRepository.findAll();
        List<DepartamentoDTO> listadepartamentosDTO =
            listadepartamentos
                .stream()
                .map(departamento -> departamentoConverter.convertToDto(departamento))
                .toList();
        return listadepartamentosDTO;
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

    //    private Post convertToEntity(PostDto postDto) throws ParseException {
//        Post post = modelMapper.map(postDto, Post.class);
//        post.setSubmissionDate(postDto.getSubmissionDateConverted(
//                userService.getCurrentUser().getPreference().getTimezone()));
//
//        if (postDto.getId() != null) {
//            Post oldPost = postService.getPostById(postDto.getId());
//            post.setRedditID(oldPost.getRedditID());
//            post.setSent(oldPost.isSent());
//        }
//        return post;
//    }


}
