package org.grupob.adminapp.service;

import org.grupob.adminapp.converter.CategoriaConverter;
import org.grupob.adminapp.dto.CategoriaDTO;
import org.grupob.comun.entity.maestras.Categoria;
import org.grupob.comun.repository.maestras.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImp {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaConverter categoriaconverter;

    public CategoriaServiceImp(CategoriaRepository categoriaRepository, CategoriaConverter categoriaconverter) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaconverter = categoriaconverter;
    }

    public List<CategoriaDTO> devuelveTodas(){
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoria -> categoriaconverter.convertirADTO(categoria))
                .collect(Collectors.toList());
    }


}
