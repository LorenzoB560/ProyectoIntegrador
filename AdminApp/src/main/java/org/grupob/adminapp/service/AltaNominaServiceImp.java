package org.grupob.adminapp.service;

import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.converter.AltaNominaConverter;
import org.grupob.comun.converter.NominaConverter;
import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.comun.dto.LineaNominaDTO;
import org.grupob.comun.entity.LineaNomina;
import org.grupob.comun.service.NominaServiceImp;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.repository.ConceptoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.LineaNominaRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AltaNominaServiceImp implements AltaNominaService {

    private final EmpleadoRepository empleadoRepository;
    private final ConceptoRepository conceptoRepository;
    private final NominaRepository nominaRepository;
    private final AltaNominaConverter nominaConverter;


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

    public Concepto devolverSalarioBase(List<Concepto> conceptos){
        return conceptos.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase("Salario base"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe 'Salario base' en la base de datos"));

    }
    public List<Concepto> devolverConceptosRestantes(List<Concepto> conceptos){
        return conceptos.stream()
                .filter(c -> !c.getNombre().equalsIgnoreCase("Salario base"))
                .toList();
    }

    public Concepto obtenerConceptoPorId(UUID id) {
        return conceptoRepository.findById(id).orElse(null);
    }


    public void guardarNomina(AltaNominaDTO altaNominaDTO){
        // El converter se encarga de crear las entidades Nomina y LineaNomina correctamente
        Nomina nomina = nominaConverter.altaNominaDTOConvierteAEntidad(altaNominaDTO);

        asignarLineasNomina(nomina, altaNominaDTO.getLineaNominas(), conceptoRepository);

        nominaRepository.save(nomina);
    }
    private void asignarLineasNomina(Nomina nomina, List<LineaNominaDTO> lineaNominas, ConceptoRepository conceptoRepository) {
        Set<LineaNomina> lineas = lineaNominas.stream().map(lineaDTO -> {
            Concepto concepto = conceptoRepository.findById(lineaDTO.getIdConcepto())
                    .orElseThrow(() -> new RuntimeException("Concepto no encontrado: " + lineaDTO.getIdConcepto()));
            LineaNomina linea = new LineaNomina();
            linea.setConcepto(concepto);
            linea.setCantidad(lineaDTO.getCantidad());
            linea.setNomina(nomina);
            linea.setPorcentaje(lineaDTO.getPorcentaje());
            return linea;
        }).collect(Collectors.toSet());

        nomina.setLineaNominas(lineas);
    }

}