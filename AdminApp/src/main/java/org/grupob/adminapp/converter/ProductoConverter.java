package org.grupob.adminapp.converter;

import org.grupob.comun.entity.Electronico;
import org.grupob.comun.entity.Libro;
import org.grupob.comun.entity.Ropa;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.adminapp.dto.ProductoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductoConverter {

    ModelMapper modelMapper;

    public ProductoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductoDTO convertToDto(Producto producto) {
        ProductoDTO dto = modelMapper.map(producto, ProductoDTO.class);
//        if (producto instanceof Libro libro) {
//            dto.setTipoProductoNombre("Libro");
//            dto.setAutor(libro.getAutor());
//            dto.setEditorial(libro.getEditorial());
//        } else if (producto instanceof Electronico electronico) {
//            dto.setTipoProductoNombre("Electrónico");
//            dto.setMarca(electronico.getMarca());
//        } else if (producto instanceof Ropa ropa) {
//            dto.setTipoProductoNombre("Ropa");
//            dto.setMaterial(ropa.getMaterial());
//        } else {
//            dto.setTipoProductoNombre("Otro");
//        }
        return dto;
    }
    }



