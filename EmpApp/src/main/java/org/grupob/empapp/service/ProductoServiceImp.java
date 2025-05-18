package org.grupob.empapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.comun.repository.ProductoRepository;
import org.grupob.empapp.converter.ProductoConverter;
import org.grupob.empapp.dto.ProductoDTO;
import org.grupob.comun.dto.ProductoSearchDTO;
import org.grupob.empapp.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProductoServiceImp implements ProductoService {



    private final ProductoRepository productoRepository;
    private final ProductoConverter productoConverter;

    @Autowired
    public ProductoServiceImp(ProductoRepository productoRepository, ProductoConverter productoConverter) {
        this.productoRepository = productoRepository;
        this.productoConverter = productoConverter;
    }

    @Override
    public ProductoDTO devuelveProducto(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        return productoConverter.entityToDTO(producto);
    }
    @Override
    public List<ProductoDTO> listarProductos() { // Cambiado a ResponseEntity<List<ProductoDTO> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoDTO> dtos = productos.stream()
                .map(productoConverter::entityToDTO)
                .toList();
        return dtos;
    }

    @Override
    public Page<ProductoDTO> buscarProductosPaginados(
            ProductoSearchDTO searchParams, int page, int size, String sortBy, String sortDir) {

        String sortProperty;

        // Mapea el valor de 'sortBy' del frontend al path correcto en JPQL
        switch (sortBy.toLowerCase()) {
            case "descripcion":
                sortProperty = "p.descripcion";
                break;
            case "categoriaprincipal":
                sortProperty = "cat.nombre";
                break;
            case "preciobase":
            case "precio":
                sortProperty = "p.precio";
                break;
            // ELIMINA O COMENTA EL SIGUIENTE CASO:
            // case "proveedor.nombre":
            //     sortProperty = "prov.nombre";
            //     break;
            default:
                List<String> camposDirectosPermitidos = Arrays.asList("id", "marca", "segundaMano", "unidades", "fechaFabricacion", "fechaAlta", "valoracion");
                if (camposDirectosPermitidos.contains(sortBy)) {
                    sortProperty = "p." + sortBy;
                } else {
                    System.err.println("Advertencia: Parámetro sortBy no soportado '" + sortBy + "'. Usando 'p.descripcion' por defecto.");
                    sortProperty = "p.descripcion";
                }
                break;
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        Page<Producto> productosPage = productoRepository.buscarProductosAdminPaginado(searchParams, pageable);

        return productosPage.map(productoConverter::entityToDTO);

    }

}