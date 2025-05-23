package org.grupob.empapp.controller;

import jakarta.persistence.EntityNotFoundException;
import org.grupob.empapp.dto.ProductoDTO;
import org.grupob.comun.dto.ProductoSearchDTO;
import org.grupob.empapp.service.ProductoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener detalles del producto.");
        }
    }

    @GetMapping("/listado")
    public ResponseEntity<Page<ProductoDTO>> listarProductos(
            ProductoSearchDTO searchParams, // Spring mapea query params a los campos de este DTO
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "descripcion") String sortBy, // Ordenar por nombre por defecto
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            // Llama al servicio pasando el DTO de búsqueda y los parámetros de paginación/ordenación
            Page<ProductoDTO> productosPaginados = productoService.buscarProductosPaginados(
                    searchParams, page, size, sortBy, sortDir);
            return ResponseEntity.ok(productosPaginados);
        } catch (IllegalArgumentException e) {
            // Capturar errores como un sortBy inválido
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parámetro de ordenación inválido.", e);
        } catch (Exception e) {
            // Loguear el error 'e' en un sistema de logs real
            System.err.println("Error inesperado al listar productos: " + e.getMessage());
            e.printStackTrace();
            // Devolver error genérico al cliente
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el listado de productos", e);
        }
    }
}