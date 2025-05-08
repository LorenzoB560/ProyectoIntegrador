package org.grupob.empapp.service;

import jakarta.persistence.EntityNotFoundException;
import org.grupob.empapp.converter.ProductoConverter;
import org.grupob.empapp.dto.ProductoDTO;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.comun.repository.ProductoRepository;
import org.grupob.empapp.dto.ProductoSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Para verificar Strings no vacíos

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
        if (!List.of("nombre", "precio", "descripcion", "fechaAlta", "id").contains(sortBy)) {
            sortBy = "nombre";
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // 2. Extraer parámetros del DTO (con chequeo null)
        String descPatron = (searchParams != null) ? searchParams.getDescripcionPatron() : null;
        Long idProv = (searchParams != null) ? searchParams.getIdProveedor() : null;
        List<Long> idsCats = (searchParams != null) ? searchParams.getIdsCategorias() : null;
        Boolean perec = (searchParams != null) ? searchParams.getSegundaMano() : null;
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
                descPatron, idProv, idsCats, perec, pMin, pMax, pageable
        );

        // 4. Mapear a DTO (igual que antes)
        return productoPage.map(productoConverter::entityToDTO);
    }
}