package org.grupob.adminapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.comun.repository.ProductoRepository;
import org.grupob.adminapp.converter.ProductoConverter;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.adminapp.dto.ProductoSearchDTO;
import org.grupob.adminapp.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        return productoConverter.convertToDto(producto);
    }
    @Override
    public List<ProductoDTO> listarProductos() { // Cambiado a ResponseEntity<List<ProductoDTO> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoDTO> dtos = productos.stream()
                .map(productoConverter::convertToDto)
                .toList();
        return dtos;
    }

    /*@Override
    public Page<ProductoDTO> buscarProductosPaginados(
            ProductoSearchDTO searchParams,
            int page, int size, String sortBy, String sortDir) {

        // Validar ordenación y crear Pageable (igual que antes)
        if (!List.of("nombre", "precio", "id").contains(sortBy)) {
            sortBy = "nombre";
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Obtener filtros del DTO (con chequeo null para DTO)
        String tipo = (searchParams != null) ? searchParams.getTipo() : null;
        String nombre = (searchParams != null) ? searchParams.getNombre() : null;
        Double precio = (searchParams != null) ? searchParams.getPrecio() : null;
        // Obtener otros filtros específicos si los añadiste al DTO y a las Queries

        // --- Decidir qué query llamar ---
        Page<? extends Producto> productoPage; // Usar wildcard para aceptar Page<Producto1>, etc.

        if (StringUtils.hasText(tipo) && !tipo.equalsIgnoreCase("Todos")) {
            if (tipo.equalsIgnoreCase("Libro")) {
                // Llamar query específica para Producto1
                productoPage = productoRepository.buscarLibroPaginado(nombre, precio, *//* otros filtros específicos si hay, *//* pageable);
            } else if (tipo.equalsIgnoreCase("Electrónico")) {
                // Llamar query específica para Producto2
                productoPage = productoRepository.buscarElectronicoPaginado(nombre, precio, *//* otros filtros específicos si hay, *//* pageable);
            } else if (tipo.equalsIgnoreCase("Ropa")) {
                // Llamar query específica para Producto3
                productoPage = productoRepository.buscarRopaPaginado(nombre, precio, *//* otros filtros específicos si hay, *//* pageable);
            } else {
                // Tipo desconocido, devolver vacío o lanzar error? Devolvemos vacío.
                productoPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
            }
        } else {
            // Si tipo es "Todos" o null, llamar a la consulta base
            productoPage = productoRepository.buscarProductosBasePaginado(nombre, precio, pageable);
        }
        // --------------------------------

        // Mapear la página resultante (de Producto, Producto1, etc.) a Page<ProductoDTO>
        // El converter se encarga de manejar los distintos tipos de Producto
        return productoPage.map(productoConverter::convertToDto);
    }
*/
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