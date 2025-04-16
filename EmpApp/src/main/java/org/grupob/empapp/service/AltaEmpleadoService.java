package org.grupob.empapp.service;

import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.entity.Empleado;
import org.grupob.empapp.entity.maestras.Genero;
import org.grupob.empapp.repository.EmpleadoRepository;
import org.grupob.empapp.repository.GeneroRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AltaEmpleadoService {

    private final GeneroRepository generoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoConverter empleadoConverter;

    public AltaEmpleadoService(GeneroRepository generoRepository, EmpleadoRepository empleadoRepository, EmpleadoConverter empleadoConverter) {
        this.generoRepository = generoRepository;
        this.empleadoRepository = empleadoRepository;
        this.empleadoConverter = empleadoConverter;
    }

    public Empleado guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO, MultipartFile archivo){
        Empleado empleado = empleadoConverter.convertirAEntidad(altaEmpleadoDTO);
        try {
            empleado.setFoto(archivo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ID de género recibido: " + altaEmpleadoDTO.getIdGeneroSeleccionado());
        Genero genero = generoRepository.findById(altaEmpleadoDTO.getIdGeneroSeleccionado()).orElseThrow();
        empleado.setGenero(genero);
        return empleadoRepository.save(empleado);
    }
}
