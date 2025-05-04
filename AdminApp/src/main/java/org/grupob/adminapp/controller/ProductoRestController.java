package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.service.ProductoCargaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("adminapp")
public class ProductoRestController {
    private final ProductoCargaService productoService;

    public ProductoRestController(ProductoCargaService productoService) {
        this.productoService = productoService;
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
}


