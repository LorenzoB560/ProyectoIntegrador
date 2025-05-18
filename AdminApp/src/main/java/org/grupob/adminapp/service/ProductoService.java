package org.grupob.adminapp.service;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.comun.dto.ProductoSearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductoService {

    List<ProductoDTO> listarProductos();
    ProductoDTO devuelveProducto(UUID id);

    Page<ProductoDTO> buscarProductosPaginados(
            ProductoSearchDTO searchParams, // Acepta DTO de EmpApp
            int page,
            int size,
            String sortBy, // Ahora puede ser 'descripcion' o 'categoria'
            String sortDir);
    void eliminarProducto(UUID id);
}