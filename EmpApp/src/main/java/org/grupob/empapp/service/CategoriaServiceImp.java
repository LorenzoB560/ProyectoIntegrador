package org.grupob.empapp.service;

import lombok.RequiredArgsConstructor;
import org.grupob.empapp.converter.CategoriaConverter;
import org.grupob.empapp.dto.CategoriaDTO;
import org.grupob.empapp.service.CategoriaService;
import org.grupob.comun.entity.maestras.Categoria;
import org.grupob.comun.repository.maestras.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImp implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaConverter categoriaconverter;

    public List<CategoriaDTO> devuelveTodas(){
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoriaconverter::convertirADTO)
                .collect(Collectors.toList());
    }


}
