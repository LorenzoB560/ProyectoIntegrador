package org.grupob.empapp.service;

import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.entity.Empleado;
import org.grupob.empapp.entity.maestras.Genero;
import org.grupob.empapp.repository.EmpleadoRepository;
import org.grupob.empapp.repository.maestras.GeneroRepository;
import org.springframework.stereotype.Service;

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

    public void guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO){
        Empleado empleado = empleadoConverter.convertirAEntidad(altaEmpleadoDTO);
        empleado.setGenero(generoRepository.findById(altaEmpleadoDTO.getIdGeneroSeleccionado()).orElseThrow());
        System.err.println(empleado);
        empleadoRepository.save(empleado);
    }
}
