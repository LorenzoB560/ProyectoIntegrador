package org.grupob.adminapp.service;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.adminapp.dto.ProductoSearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductoService {

    List<ProductoDTO> listarProductos();
    ProductoDTO devuelveProducto(UUID id);

    Page<ProductoDTO> buscarProductosPaginados(
            ProductoSearchDTO searchParams, // <-- Usar el DTO
            int page, int size, String sortBy, String sortDir);
}