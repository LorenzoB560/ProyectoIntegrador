package org.grupob.empapp.service;

import org.grupob.comun.entity.Departamento;
import org.grupob.comun.entity.maestras.Genero;
import org.grupob.comun.entity.maestras.Pais;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.comun.repository.PaisRepository;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AltaEmpleadoServiceImp implements AltaEmpleadoService {

    private final GeneroRepository generoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PaisRepository paisRepository;
    private final EmpleadoConverter empleadoConverter;
    private final DepartamentoRepository departamentoRepository;

    public AltaEmpleadoServiceImp(GeneroRepository generoRepository, EmpleadoRepository empleadoRepository, PaisRepository paisRepository, EmpleadoConverter empleadoConverter, DepartamentoRepository departamentoRepository) {
        this.generoRepository = generoRepository;
        this.empleadoRepository = empleadoRepository;
        this.paisRepository = paisRepository;
        this.empleadoConverter = empleadoConverter;
        this.departamentoRepository = departamentoRepository;
    }

    public List<Genero> devolverGeneros() {
        return generoRepository.findAll();
    }
    public List<Pais> devolverPaises(){
        return paisRepository.findAll();
    }
    public List<Departamento> devolverDepartamentos() {
        return departamentoRepository.findAll();
    }

    public void guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO){
        Empleado empleado = empleadoConverter.convertirAEntidad(altaEmpleadoDTO);
        empleado.setGenero(generoRepository.findById(altaEmpleadoDTO.getIdGeneroSeleccionado()).orElseThrow());
        System.err.println(empleado);
        empleadoRepository.save(empleado);
    }

}
