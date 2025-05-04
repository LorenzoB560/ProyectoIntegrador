package org.grupob.adminapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupob.adminapp.converter.ElectronicoConverter;
import org.grupob.adminapp.converter.LibroConverter;
import org.grupob.adminapp.converter.RopaConverter;
import org.grupob.adminapp.dto.ElectronicoDTO;
import org.grupob.adminapp.dto.LibroDTO;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.adminapp.dto.RopaDTO;
// Asume que CargaMasivaException está definida en otro archivo, ej. en un paquete de excepciones
import org.grupob.comun.exception.CargaMasivaException;
import org.grupob.comun.repository.ElectronicoRepository;
import org.grupob.comun.repository.LibroRepository;
import org.grupob.comun.repository.RopaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class ProductoCargaService {

    private final LibroConverter libroConverter;
    private final ElectronicoConverter electronicoConverter;
    private final RopaConverter ropaConverter;

    private final LibroRepository libroRepository;
    private final ElectronicoRepository electronicoRepository;
    private final RopaRepository ropaRepository;

    public ProductoCargaService(
            LibroConverter libroConverter,
            ElectronicoConverter electronicoConverter,
            RopaConverter ropaConverter,
            LibroRepository libroRepository,
            ElectronicoRepository electronicoRepository,
            RopaRepository ropaRepository) {
        this.libroConverter = libroConverter;
        this.electronicoConverter = electronicoConverter;
        this.ropaConverter = ropaConverter;
        this.libroRepository = libroRepository;
        this.electronicoRepository = electronicoRepository;
        this.ropaRepository = ropaRepository;
    }

    @Transactional(rollbackFor = Exception.class) // Revierte si CUALQUIER excepción ocurre
    public void cargaMasiva(InputStream jsonInput) throws CargaMasivaException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductoDTO[] productos = mapper.readValue(jsonInput, ProductoDTO[].class);

            Arrays.stream(productos).forEach(dto -> {
                // El try-catch interno se omite aquí para que cualquier error
                // dentro del stream detenga la transacción completa y se propague.
                if (dto instanceof LibroDTO libroDTO) {
                    libroRepository.save(libroConverter.convertirAEntidad(libroDTO));
                } else if (dto instanceof ElectronicoDTO electronicoDTO) {
                    electronicoRepository.save(electronicoConverter.convertirAEntidad(electronicoDTO));
                } else if (dto instanceof RopaDTO ropaDTO) {
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
            // Es buena idea loguear 'e' aquí en un sistema real antes de lanzar
            throw new CargaMasivaException("Error inesperado durante el procesamiento de la carga masiva.");
        }
    }
}
