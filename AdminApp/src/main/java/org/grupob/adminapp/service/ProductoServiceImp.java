package org.grupob.adminapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.comun.repository.ProductoRepository;
import org.grupob.adminapp.converter.ProductoConverter;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.comun.dto.ProductoSearchDTO;
import org.grupob.adminapp.service.ProductoService;
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
    @Override
    @Transactional // Asegura atomicidad en la operación de borrado
    public void eliminarProducto(UUID id) {
        if (!productoRepository.existsById(id)) {
            // Lanza excepción si el producto no existe para informar al controlador
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        try {
            productoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // Esto puede ocurrir si el producto se eliminó entre el existsById y el delete,
            // aunque es raro con @Transactional. Lo relanzamos como not found.
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id + " (posiblemente eliminado por otro proceso)");
        }
        // Nota: Si tienes relaciones (ej: Producto en Pedidos), puede que necesites
        // manejar `DataIntegrityViolationException` o configurar el borrado en cascada (con cuidado).
    }
}