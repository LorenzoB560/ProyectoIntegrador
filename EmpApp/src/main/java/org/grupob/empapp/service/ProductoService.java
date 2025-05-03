package org.grupob.empapp.service;
import org.grupob.empapp.dto.ProductoDTO;

import java.util.List;
import java.util.UUID;

public interface ProductoService {

    List<ProductoDTO> listarProductos();
    ProductoDTO devuelveProducto(UUID id);
}