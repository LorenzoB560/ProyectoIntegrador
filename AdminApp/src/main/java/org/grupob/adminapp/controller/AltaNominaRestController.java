package org.grupob.adminapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
//@RequestMapping("/adminapp")
@RequiredArgsConstructor
public class AltaNominaRestController {

    private final AltaNominaServiceImp altaNominaServiceImp;
    private final NominaRepository nominaRepository;


    @PostMapping("/guardar-nomina")
    @ResponseBody
    public ResponseEntity<?> guardarNomina(@RequestBody @Valid AltaNominaDTO altaNominaDTO) {
        try {
            // Validar que existan elementos en la nómina
            if (altaNominaDTO.getLineaNominas() == null || altaNominaDTO.getLineaNominas().isEmpty()) {
                return ResponseEntity.badRequest().body("La nómina debe contener al menos un concepto");
            }

            // Validar que exista el concepto de salario base
            boolean tieneSalarioBase = altaNominaDTO.getLineaNominas().stream()
                    .anyMatch(linea -> {
                        Concepto concepto = altaNominaServiceImp.obtenerConceptoPorId(linea.getIdConcepto());
                        return concepto != null && concepto.getNombre().equalsIgnoreCase("Salario base");
                    });

            if (!tieneSalarioBase) {
                return ResponseEntity.badRequest().body("La nómina debe incluir el concepto 'Salario base'");
            }


            // Validación para que la fecha de fin sea posterior a la fecha de inicio
            LocalDate fechaInicio = altaNominaDTO.getPeriodo().getFechaInicio();
            LocalDate fechaFin = altaNominaDTO.getPeriodo().getFechaFin();

            if (fechaInicio.isAfter(fechaFin) || fechaInicio.isEqual(fechaFin)) {
                //Añado el error al controlador de errores
                return ResponseEntity.badRequest().body(Map.of("listaErrores", List.of("La fecha de inicio debe ser anterior a la fecha de fin")));
            }

            // Obtener nóminas anteriores del empleado
            List<Nomina> nominasExistentes = nominaRepository.findNominasByEmpleadoId(altaNominaDTO.getIdEmpleado());

            Optional<Nomina> solapada = nominasExistentes.stream()
                    .filter(n -> {
                        LocalDate fechaAnteriorInicio = n.getPeriodo().getFechaInicio();
                        LocalDate fechaPosteriorFin = n.getPeriodo().getFechaFin();
                        //Con esto verifico que las fechas no se solapen
                        // Verifico si la fecha de fin se solapa con la fecha anterior de fin, devuelvo true
                        // Sila fecha de inicio se solapa con la fecha anterior de fin, devuelvo true
                        return !(fechaFin.isBefore(fechaAnteriorInicio) || fechaInicio.isAfter(fechaPosteriorFin));
                    })
                    .findFirst();

            //Obtengo la nómina sacada del stream, y envío el mensaje
            if (solapada.isPresent()) {
                Nomina n = solapada.get();
                LocalDate fechaAnteriorInicio = n.getPeriodo().getFechaInicio();
                LocalDate fechaPosteriorFin = n.getPeriodo().getFechaFin();
                return ResponseEntity.badRequest().body(Map.of("listaErrores", List.of(
                        "El período de la nueva nómina se solapa con una ya existente: " +
                                fechaAnteriorInicio + " - " + fechaPosteriorFin)));
            }


            // Validar que sea posterior a la última nómina
            Optional<Nomina> ultimaNomina = nominasExistentes.stream()
                    .max(Comparator.comparing(n -> n.getPeriodo().getFechaFin()));

            if (ultimaNomina.isPresent()) {
                LocalDate ultimaFin = ultimaNomina.get().getPeriodo().getFechaFin();

                if (!fechaInicio.isAfter(ultimaFin) || !fechaFin.isAfter(ultimaFin)) {
                    return ResponseEntity.badRequest().body(Map.of("listaErrores", List.of(
                            "La fecha de inicio y fin deben ser posteriores a la fecha de fin de la última nómina: " + ultimaFin)));
                }
            }
            // Guardar la nómina

            System.out.println(altaNominaDTO);
            altaNominaServiceImp.guardarNomina(altaNominaDTO);
            return ResponseEntity.ok().body(Map.of("mensaje", "Nómina guardada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la nómina: " + e.getMessage());
        }
    }
}
