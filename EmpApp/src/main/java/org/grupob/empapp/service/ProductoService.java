package org.grupob.empapp.service;
import org.grupob.empapp.dto.ProductoDTO;
import org.grupob.comun.dto.ProductoSearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductoService {

    List<ProductoDTO> listarProductos();
    ProductoDTO devuelveProducto(UUID id);

    Page<ProductoDTO> buscarProductosPaginados(
            ProductoSearchDTO searchParams, int page, int size, String sortBy, String sortDir);
}