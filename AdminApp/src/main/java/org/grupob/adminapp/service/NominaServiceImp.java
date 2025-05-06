package org.grupob.adminapp.service;

import org.grupob.adminapp.converter.NominaConverter;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NominaServiceImp implements NominaService{

    private final NominaRepository nominaRepository;
    private final NominaConverter nominaConverter;
    private final EmpleadoRepository empleadoRepository;

    public NominaServiceImp(NominaRepository nominaRepository, NominaConverter nominaConverter, EmpleadoRepository empleadoRepository) {
        this.nominaRepository = nominaRepository;
        this.nominaConverter = nominaConverter;
        this.empleadoRepository = empleadoRepository;
    }

    public List<NominaDTO> devolverNominas(){
        List<Nomina> nominas = nominaRepository.findAll();
        List<NominaDTO> nominasDTO = nominas.stream()
                .map(nominaConverter::convierteADTO)
                .toList();


        nominasDTO = nominasDTO.stream()
                .peek(nomina -> {
                    Optional<Empleado> empleado = empleadoRepository.findById(nomina.getIdEmpleado());
                    empleado.ifPresent(value -> nomina.setNombre(value.getNombre() + " " + value.getApellido()));
                }).toList();

        System.out.println(nominasDTO);
        return nominasDTO;
    }
}
