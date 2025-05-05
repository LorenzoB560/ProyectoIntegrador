package org.grupob.adminapp.controller;

import jakarta.persistence.EntityNotFoundException;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.adminapp.dto.ProductoSearchDTO;
import org.grupob.adminapp.service.ProductoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/productos") // Ruta base para API REST (plural)
public class ProductoRestController {

    private final ProductoServiceImp productoService;

    @Autowired
    public ProductoRestController(ProductoServiceImp productoService) {
        this.productoService = productoService;
    }
    @GetMapping("/listado1")
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> getDetalleProducto(@PathVariable UUID id) {
        try {
            ProductoDTO productoDTO = productoService.devuelveProducto(id);
            return ResponseEntity.ok(productoDTO); // Devuelve DTO como JSON
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Considera añadir logging aquí
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener detalles del producto.");
        }
    }
    @GetMapping("/listado")
    public ResponseEntity<Page<ProductoDTO>> listarProductos(
            ProductoSearchDTO searchParams, // <-- Recibe el DTO (Spring mapea params URL a campos)
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            // Llama al servicio pasando el DTO
            Page<ProductoDTO> productosPaginados = productoService.buscarProductosPaginados(
                    searchParams, page, size, sortBy, sortDir);
            return ResponseEntity.ok(productosPaginados);
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}