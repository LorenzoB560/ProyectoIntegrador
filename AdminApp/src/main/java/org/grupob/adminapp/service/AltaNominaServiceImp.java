package org.grupob.adminapp.service;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AltaNominaServiceImp implements AltaNominaService {

    private final EmpleadoRepository empleadoRepository;

    public AltaNominaServiceImp(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public List<Empleado> devuelveEmpleados(){
        return empleadoRepository.findAll();
    }

    public List<String> devolverMeses(){
        return IntStream.rangeClosed(1, 12)
                .mapToObj(m -> String.format("%02d", m))
                .collect(Collectors.toList());
    }
    public List<String> devolverAnios(){
        int anioActual = Year.now().getValue();
        return IntStream.range(anioActual, anioActual + 2)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }
}
