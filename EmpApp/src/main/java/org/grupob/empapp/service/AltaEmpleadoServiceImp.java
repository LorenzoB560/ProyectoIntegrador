package org.grupob.empapp.service;

import org.grupob.comun.entity.Departamento;
import org.grupob.comun.entity.EntidadBancaria;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.entity.maestras.*;
import org.grupob.comun.repository.*;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AltaEmpleadoServiceImp implements AltaEmpleadoService {

    private final GeneroRepository generoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PaisRepository paisRepository;
    private final TipoViaRepository tipoViaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final EntidadBancariaRepository entidadBancariaRepository;
    private final TipoTarjetaRepository tipoTarjetaRepository;

    private final EmpleadoConverter empleadoConverter;

    public AltaEmpleadoServiceImp(GeneroRepository generoRepository, EmpleadoRepository empleadoRepository, PaisRepository paisRepository, TipoViaRepository tipoViaRepository, EmpleadoConverter empleadoConverter, DepartamentoRepository departamentoRepository, TipoDocumentoRepository tipoDocumentoRepository, TipoDocumentoRepository tipoDocumentoRepository1, EspecialidadRepository especialidadRepository, EntidadBancariaRepository entidadBancariaRepository, TipoTarjetaRepository tipoTarjetaRepository) {
        this.generoRepository = generoRepository;
        this.empleadoRepository = empleadoRepository;
        this.paisRepository = paisRepository;
        this.tipoViaRepository = tipoViaRepository;
        this.empleadoConverter = empleadoConverter;
        this.departamentoRepository = departamentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository1;
        this.especialidadRepository = especialidadRepository;
        this.entidadBancariaRepository = entidadBancariaRepository;
        this.tipoTarjetaRepository = tipoTarjetaRepository;
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
    public List<Especialidad> devolverEspecialidades() {
        return especialidadRepository.findAll();
    }
    public List<EntidadBancaria> devolverEntidadesBancarias(){
        return entidadBancariaRepository.findAll();
    }
    public List<TipoTarjetaCredito> devolverTipoTarjetasCredito(){
        return tipoTarjetaRepository.findAll();
    }

    public List<String> devolverMeses(){
        return IntStream.rangeClosed(1, 12)
                .mapToObj(m -> String.format("%02d", m))
                .collect(Collectors.toList());
    }
    public List<String> devolverAnios(){
        int anioActual = Year.now().getValue();
       return IntStream.range(anioActual, anioActual + 21)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }


    public void guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO){
        Empleado empleado = empleadoConverter.convertirAEntidad(altaEmpleadoDTO);
        empleado.setGenero(generoRepository.findById(altaEmpleadoDTO.getIdGeneroSeleccionado()).orElseThrow());
        System.err.println(empleado);
        empleadoRepository.save(empleado);
    }

}
