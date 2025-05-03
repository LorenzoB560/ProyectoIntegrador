package org.grupob.adminapp.controller;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/adminapp")
public class AltaNominaRestController {

    private NominaRepository nominaRepository;


    private EmpleadoRepository empleadoRepository;

//    @PostMapping("/guardar-nomina")
//    public ResponseEntity<String> crearNomina(@RequestBody AltaNominaDTO dto) {
//        Optional<Empleado> empleadoOpt = empleadoRepository.findById(dto.getEmpleadoId());
//
//        if (empleadoOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado");
//        }
//
//        Nomina nomina = new Nomina();
//        nomina.setEmpleado(empleadoOpt.get());
//        nomina.setFecha(dto.getFecha());
//        nomina.setSalario(dto.getSalario());
//
//        nominaRepository.save(nomina);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("Nómina creada con éxito");
//    }
}
