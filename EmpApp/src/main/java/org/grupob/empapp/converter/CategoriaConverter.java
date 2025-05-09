package org.grupob.empapp.converter;

import org.grupob.empapp.dto.CategoriaDTO;
import org.grupob.comun.entity.maestras.Categoria;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoriaConverter {
    ModelMapper modelMapper;


    public CategoriaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoriaDTO convertirADTO(Categoria categoria) {
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    public Categoria convertirAEntidad(CategoriaDTO dto) {
        return modelMapper.map(dto, Categoria.class);
    }
}
