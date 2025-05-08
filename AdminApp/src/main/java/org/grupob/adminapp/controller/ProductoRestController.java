package org.grupob.adminapp.controller;

import jakarta.persistence.EntityNotFoundException;
import org.grupob.adminapp.dto.CategoriaDTO;
import org.grupob.adminapp.dto.masiva.ProductoCargaDTO;
import org.grupob.adminapp.service.CategoriaServiceImp;
import org.grupob.adminapp.service.ProductoMasivoService;
import org.grupob.adminapp.service.ProductoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/productos") // Ruta base para API REST (plural)
public class ProductoRestController {

    private final ProductoServiceImp productoService;
    private final CategoriaServiceImp categoriaService;
    private final ProductoMasivoService productoMasivoService;

    @Autowired
    public ProductoRestController(ProductoServiceImp productoService, CategoriaServiceImp categoriaService, ProductoMasivoService productoMasivoService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.productoMasivoService = productoMasivoService;
    }
    @GetMapping("/listado1")
    public ResponseEntity<List<ProductoCargaDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> getDetalleProducto(@PathVariable UUID id) {
        try {
            ProductoCargaDTO productoDTO = productoService.devuelveProducto(id);
            return ResponseEntity.ok(productoDTO); // Devuelve DTO como JSON
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Considera añadir logging aquí
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener detalles del producto.");
        }
    }
   /* @GetMapping("/listado")
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
    }*/
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable UUID id) {
        // Aquí podrías añadir @PreAuthorize("hasRole('ADMIN')") si usas Spring Security
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
    public ResponseEntity<String> cargarMasivaProductos(@RequestParam(value = "archivo", required = false) MultipartFile archivo) {

        if(archivo==null){
            return ResponseEntity.badRequest().body("Debes seleccionar un archivo JSON");
        }

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo proporcionado está vacío.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE) // 415 Unsupported Media Type
                    .body("El tipo de archivo no es JSON.");
        }

        try (InputStream input = archivo.getInputStream()) {
            productoMasivoService.cargaMasiva(input);
            return ResponseEntity.ok("Carga completada correctamente.");
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Error al leer el archivo: " + e.getMessage());
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
