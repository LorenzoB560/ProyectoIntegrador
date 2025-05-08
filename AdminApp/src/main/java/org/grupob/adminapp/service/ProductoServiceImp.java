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

import java.math.BigDecimal;
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

        // 1. Validar ordenación y crear Pageable (igual que antes)
        if (!List.of("nombre", "precio", "descripcion", "fechaAlta", "id",
                "proveedor.nombre", "categorias.nombre").contains(sortBy)) { // <-- Nuevos campos
            sortBy = "descripcion"; // O el default que prefieras
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // 2. Extraer parámetros del DTO (con chequeo null)
        String descPatron = (searchParams != null) ? searchParams.getDescripcionPatron() : null;
        Long idProv = (searchParams != null) ? searchParams.getIdProveedor() : null;
        List<Long> idsCats = (searchParams != null) ? searchParams.getIdsCategorias() : null;
        Boolean segM = (searchParams != null) ? searchParams.getSegundaMano() : null;
        BigDecimal pMin = (searchParams != null) ? searchParams.getPrecioMin() : null;
        BigDecimal pMax = (searchParams != null) ? searchParams.getPrecioMax() : null;

        // --- Manejo especial para lista de categorías vacía ---
        // La cláusula 'cat.id IN (:idsCats)' puede fallar o comportarse raro con listas vacías en algunas BD/JPA.
        // Es más seguro pasar null al repositorio si la lista está vacía.
        if (idsCats != null && idsCats.isEmpty()) {
            idsCats = null; // JPQL/SQL maneja mejor 'param IS NULL' que 'col IN ()'
        }
        // ----------------------------------------------------


        // 3. Llamar al método del repositorio con @Query
        Page<Producto> productoPage = productoRepository.buscarProductosAdminPaginado(
                descPatron, idProv, idsCats, segM, pMin, pMax, pageable
        );

        // 4. Mapear a DTO (igual que antes)
        return productoPage.map(productoConverter::entityToDTO);
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