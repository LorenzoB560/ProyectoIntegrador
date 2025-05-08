package org.grupob.adminapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupob.adminapp.converter.ElectronicoConverter;
import org.grupob.adminapp.converter.LibroConverter;
import org.grupob.adminapp.converter.MuebleConverter;
import org.grupob.adminapp.converter.RopaConverter;
import org.grupob.adminapp.dto.masiva.ElectronicoCargaDTO;
import org.grupob.adminapp.dto.masiva.LibroCargaDTO;
import org.grupob.adminapp.dto.masiva.ProductoCargaDTO;
import org.grupob.adminapp.dto.masiva.RopaCargaDTO;
import org.grupob.comun.exception.CargaMasivaException;
import org.grupob.comun.exception.CategoriaNoEncontradaException;
import org.grupob.comun.repository.ElectronicoRepository;
import org.grupob.comun.repository.LibroRepository;
import org.grupob.comun.repository.ProductoRepository;
import org.grupob.comun.repository.RopaRepository;
import org.grupob.comun.repository.maestras.CategoriaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class ProductoMasivoService {

    private final LibroConverter libroConverter;
    private final ElectronicoConverter electronicoConverter;
    private final RopaConverter ropaConverter;
    private final MuebleConverter muebleConverter;

    private final ProductoRepository productoRepository;
    private final LibroRepository libroRepository;
    private final ElectronicoRepository electronicoRepository;
    private final RopaRepository ropaRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoMasivoService(
            LibroConverter libroConverter,
            ElectronicoConverter electronicoConverter,
            RopaConverter ropaConverter, MuebleConverter muebleConverter,
            ProductoRepository productoRepository,
            LibroRepository libroRepository,
            ElectronicoRepository electronicoRepository,
            RopaRepository ropaRepository, CategoriaRepository categoriaRepository) {
        this.libroConverter = libroConverter;
        this.electronicoConverter = electronicoConverter;
        this.ropaConverter = ropaConverter;
        this.muebleConverter = muebleConverter;
        this.productoRepository = productoRepository;
        this.libroRepository = libroRepository;
        this.electronicoRepository = electronicoRepository;
        this.ropaRepository = ropaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(rollbackFor = Exception.class) // Revierte si CUALQUIER excepción ocurre
    public void cargaMasiva(InputStream jsonInput) throws CargaMasivaException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductoCargaDTO[] productos = mapper.readValue(jsonInput, ProductoCargaDTO[].class);

            Arrays.stream(productos).forEach(dto -> {
                // El try-catch interno se omite aquí para que cualquier error
                // dentro del stream detenga la transacción completa y se propague.
                if (dto instanceof LibroCargaDTO libroDTO) {
                    libroRepository.save(libroConverter.convertirAEntidad(libroDTO));
                } else if (dto instanceof ElectronicoCargaDTO electronicoDTO) {
                    electronicoRepository.save(electronicoConverter.convertirAEntidad(electronicoDTO));
                } else if (dto instanceof RopaCargaDTO ropaDTO) {
                    ropaRepository.save(ropaConverter.convertirAEntidad(ropaDTO));
                } else {
                    // Si llega aquí, Jackson deserializó algo inesperado
                    throw new IllegalArgumentException("Tipo de producto DTO desconocido encontrado: " + dto.getClass().getSimpleName());
                }
            });

        } catch (JsonProcessingException e) { // Captura errores específicos de Jackson (parseo, mapeo)
            throw new CargaMasivaException("Error en el formato o contenido del archivo JSON.");
        } catch (IOException e) { // Captura otros errores de I/O durante la lectura inicial
            throw new CargaMasivaException("Error leyendo el archivo.");
        } catch (DataAccessException e) { // Captura errores de base de datos (ej. violación de constraint)
            throw new CargaMasivaException("Error interactuando con la base de datos durante el guardado.");
        } catch (IllegalArgumentException e) { // Captura el error de tipo desconocido u otros errores de validación interna
            throw new CargaMasivaException("Error en los datos proporcionados: " + e.getMessage());
        } catch (Exception e) { // Captura cualquier otra excepción inesperada (ej. de los converters)
            throw new CargaMasivaException("Error inesperado durante el procesamiento de la carga masiva.");
        }
    }

    @Transactional // <-- ¡Esta anotación es obligatoria para ejecutar consultas de borrado/actualización!
    public void borradoMasivo(String opcion){

        if(opcion.equals(0)){
            eliminarTodos();
        }else{
            eliminarPorCategoria(opcion);
        }

    }


    // Elimina todos los productos
    private void eliminarTodos() {
        productoRepository.deleteAll();
    }

    private void eliminarPorCategoria(String  categoriaId) {
        Long id;
        try {
            id = Long.parseLong(categoriaId);
        } catch (NumberFormatException e) {
            throw new CategoriaNoEncontradaException("Formato no aceptado");
        }
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNoEncontradaException("No existe esa categoria");
        }
        productoRepository.deleteByCategoriaId(id);
    }

}
