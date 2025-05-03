package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.LibroDTO;
import org.grupob.comun.entity.Libro;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LibroConverter {
    private final ModelMapper modelMapper;

    public LibroConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public LibroDTO convertirADto(Libro entidad) {
        return modelMapper.map(entidad, LibroDTO.class);
    }

    public Libro convertirAEntidad(LibroDTO dto) {
        return modelMapper.map(dto, Libro.class);
    }
}
