package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.CategoriaDTO;
import org.grupob.adminapp.service.CategoriaServiceImp;
import org.grupob.adminapp.service.ProductoMasivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("adminapp")
public class ProductoRestController {
    private final ProductoMasivoService productoService;
    private final CategoriaServiceImp categoriaService;

    public ProductoRestController(ProductoMasivoService productoService, CategoriaServiceImp categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
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
            productoService.cargaMasiva(input);
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
        productoService.borradoMasivo(categoriaId);
        return ResponseEntity.ok().body("Productos eliminados correctamente");
    }


}


