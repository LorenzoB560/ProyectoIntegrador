package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.masiva.LibroCargaDTO;
import org.grupob.comun.entity.Libro;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LibroConverter {
    private final ModelMapper modelMapper;

    public LibroConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public LibroCargaDTO convertirADto(Libro entidad) {
        return modelMapper.map(entidad, LibroCargaDTO.class);
    }

    public Libro convertirAEntidad(LibroCargaDTO dto) {
        return modelMapper.map(dto, Libro.class);
    }
}
