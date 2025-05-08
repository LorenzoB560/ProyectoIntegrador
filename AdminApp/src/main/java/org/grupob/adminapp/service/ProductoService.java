package org.grupob.adminapp.service;
import org.grupob.adminapp.dto.masiva.ProductoCargaDTO;

import java.util.List;
import java.util.UUID;

public interface ProductoService {

    List<ProductoCargaDTO> listarProductos();
    ProductoCargaDTO devuelveProducto(UUID id);

    /*Page<ProductoDTO> buscarProductosPaginados(
            ProductoSearchDTO searchParams, // <-- Usar el DTO
            int page, int size, String sortBy, String sortDir);*/
    void eliminarProducto(UUID id);
}