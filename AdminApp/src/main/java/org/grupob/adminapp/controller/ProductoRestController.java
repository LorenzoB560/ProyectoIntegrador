package org.grupob.adminapp.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.dto.CategoriaDTO;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.comun.dto.ProductoSearchDTO;
import org.grupob.adminapp.service.CategoriaServiceImp;
import org.grupob.adminapp.service.ProductoMasivoServiceImp;
import org.grupob.adminapp.service.ProductoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/productos") // Ruta base para API REST (plural)
@RequiredArgsConstructor
public class ProductoRestController {

    private final ProductoServiceImp productoService;
    private final CategoriaServiceImp categoriaService;
    private final ProductoMasivoServiceImp productoMasivoService;

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
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable UUID id) {
        try {
            productoService.eliminarProducto(id);
            // Éxito: Devolver 204 No Content (estándar para DELETE exitoso)
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            // Si el servicio lanza esta excepción, devolver 404 Not Found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            // Otros errores inesperados, devolver 500 Internal Server Error
            // Loggear el error e.getMessage() o e
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el producto", e);
        }
    }

    @PostMapping("/carga-masiva")
    public ResponseEntity<?> cargarMasivaProductos(@RequestParam(value = "archivo", required = false) MultipartFile archivo) {

        if(archivo==null){
            return ResponseEntity.badRequest().body("Debes seleccionar un archivo JSON");
        }

        // 1. Validación básica del archivo
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El archivo está vacío"));
        }

        if (!archivo.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Map.of("error", "Solo se admiten archivos JSON"));
        }

        try (InputStream inputStream = archivo.getInputStream()) {
            // 2. Procesamiento
            productoMasivoService.cargaMasiva(inputStream);

            return ResponseEntity.ok()
                    .body(Map.of("mensaje", "Carga masiva completada"));

        } catch (IOException e) {
            // 4. Errores de lectura del archivo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error procesando el archivo"));
        }
    }

    // Endpoint para obtener todas las categorías
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias() {
        List<CategoriaDTO> categorias = categoriaService.devuelveTodas();
        return ResponseEntity.ok(categorias);
    }

    // Endpoint para borrado masivo
    @DeleteMapping("/borrado-masivo")
    public ResponseEntity<?> borradoMasivo(@RequestParam("categoria") String categoriaId) {
        productoMasivoService.borradoMasivo(categoriaId);
        return ResponseEntity.ok().body("Productos eliminados correctamente");
    }
}
