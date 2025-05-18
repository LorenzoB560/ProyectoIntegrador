package org.grupob.empapp.service;

import org.grupob.empapp.converter.CategoriaConverter;
import org.grupob.empapp.dto.CategoriaDTO;
import org.grupob.empapp.service.CategoriaService;
import org.grupob.comun.entity.maestras.Categoria;
import org.grupob.comun.repository.maestras.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImp implements CategoriaService {

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
