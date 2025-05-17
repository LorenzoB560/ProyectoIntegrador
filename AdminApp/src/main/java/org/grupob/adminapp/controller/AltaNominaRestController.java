package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.adminapp.service.AltaNominaServiceImp;
import org.grupob.comun.entity.maestras.Concepto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
//@RequestMapping("/adminapp")
public class AltaNominaRestController {

    private final AltaNominaServiceImp altaNominaServiceImp;

    public AltaNominaRestController(AltaNominaServiceImp altaNominaServiceImp) {
        this.altaNominaServiceImp = altaNominaServiceImp;
    }

    @PostMapping("/guardar-nomina")
    @ResponseBody
    public ResponseEntity<?> guardarNomina(@RequestBody AltaNominaDTO altaNominaDTO) {
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

            // Guardar la nómina

            System.out.println(altaNominaDTO);
            altaNominaServiceImp.guardarNomina(altaNominaDTO);
            return ResponseEntity.ok().body(Map.of("mensaje", "Nómina guardada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la nómina: " + e.getMessage());
        }
    }
}
