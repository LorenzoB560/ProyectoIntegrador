package org.grupob.empapp.service;

import org.grupob.comun.entity.Departamento;
import org.grupob.comun.entity.maestras.Genero;
import org.grupob.comun.entity.maestras.Pais;
import org.grupob.comun.entity.maestras.TipoDocumento;
import org.grupob.comun.entity.maestras.TipoVia;
import org.grupob.comun.repository.*;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AltaEmpleadoServiceImp implements AltaEmpleadoService {

    private final GeneroRepository generoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PaisRepository paisRepository;
    private final TipoViaRepository tipoViaRepository;
    private final EmpleadoConverter empleadoConverter;
    private final DepartamentoRepository departamentoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public AltaEmpleadoServiceImp(GeneroRepository generoRepository, EmpleadoRepository empleadoRepository, PaisRepository paisRepository, TipoViaRepository tipoViaRepository, EmpleadoConverter empleadoConverter, DepartamentoRepository departamentoRepository, TipoDocumentoRepository tipoDocumentoRepository, TipoDocumentoRepository tipoDocumentoRepository1) {
        this.generoRepository = generoRepository;
        this.empleadoRepository = empleadoRepository;
        this.paisRepository = paisRepository;
        this.tipoViaRepository = tipoViaRepository;
        this.empleadoConverter = empleadoConverter;
        this.departamentoRepository = departamentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository1;
    }

    public List<Genero> devolverGeneros() {
        return generoRepository.findAll();
    }
    public List<Pais> devolverPaises(){
        return paisRepository.findAll();
    }
    public List<TipoVia> devolverTipoVias(){
        return tipoViaRepository.findAll();
    }
    public List<TipoDocumento> devolverTipoDocumentos(){
        return tipoDocumentoRepository.findAll();
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
