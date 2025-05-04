package org.grupob.adminapp.service;

import org.grupob.adminapp.converter.NominaConverter;
import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.LineaNomina;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.repository.ConceptoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.LineaNominaRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AltaNominaServiceImp implements AltaNominaService {

    private final EmpleadoRepository empleadoRepository;
    private final ConceptoRepository conceptoRepository;
    private final NominaRepository nominaRepository;
    private final LineaNominaRepository lineaNominaRepository;
    private final NominaConverter nominaConverter;

    public AltaNominaServiceImp(EmpleadoRepository empleadoRepository, ConceptoRepository conceptoRepository, NominaConverter nominaConverter, NominaRepository nominaRepository, LineaNominaRepository lineaNominaRepository) {
        this.empleadoRepository = empleadoRepository;
        this.conceptoRepository = conceptoRepository;
        this.nominaConverter = nominaConverter;
        this.nominaRepository = nominaRepository;
        this.lineaNominaRepository = lineaNominaRepository;
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

    public List<Concepto> devolverConcepto(){
        return conceptoRepository.findAll();
    }

    @Transactional
    public void guardarNomina(AltaNominaDTO altaNominaDTO){
        // Verificar datos recibidos para depuración
        System.out.println("AltaNominaDTO recibido: " + altaNominaDTO);
        System.out.println("Líneas recibidas: " + (altaNominaDTO.getLineaNominas() != null ? altaNominaDTO.getLineaNominas().size() : "null"));

        // El converter se encarga de crear las entidades Nomina y LineaNomina correctamente
        Nomina nomina = nominaConverter.convierteAEntidad(altaNominaDTO);

        // Guardar la nómina y sus líneas en una sola transacción
        nominaRepository.save(nomina);


        // Para depuración, imprimir la nómina guardada
        System.out.println("Nómina guardada con ID: " + nomina.getId());
        System.out.println("Líneas guardadas: " + (nomina.getLineaNominas() != null ? nomina.getLineaNominas().size() : "null"));
    }
}