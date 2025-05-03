package org.grupob.adminapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupob.adminapp.converter.ElectronicoConverter;
import org.grupob.adminapp.converter.LibroConverter;
import org.grupob.adminapp.converter.RopaConverter;
import org.grupob.adminapp.dto.ElectronicoDTO;
import org.grupob.adminapp.dto.LibroDTO;
import org.grupob.adminapp.dto.ProductoDTO;
import org.grupob.adminapp.dto.RopaDTO;
import org.grupob.comun.entity.Electronico;
import org.grupob.comun.entity.Libro;
import org.grupob.comun.entity.Ropa;
import org.grupob.comun.repository.ElectronicoRepository;
import org.grupob.comun.repository.LibroRepository;
import org.grupob.comun.repository.RopaRepository;
import org.springframework.stereotype.Service;

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

    public void cargarDesdeJson(InputStream jsonInput) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductoDTO[] productos = mapper.readValue(jsonInput, ProductoDTO[].class);

            Arrays.stream(productos).forEach(dto -> {
                if (dto instanceof LibroDTO libroDTO) {
                    libroRepository.save(libroConverter.convertirAEntidad(libroDTO));
                } else if (dto instanceof ElectronicoDTO electronicoDTO) {
                    electronicoRepository.save(electronicoConverter.convertirAEntidad(electronicoDTO));
                } else if (dto instanceof RopaDTO ropaDTO) {
                    ropaRepository.save(ropaConverter.convertirAEntidad(ropaDTO));
                } else {
                    throw new IllegalArgumentException("Tipo de producto desconocido: " + dto.getClass().getSimpleName());
                }
            });
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo JSON: " + e.getMessage());
        }
    }
}
