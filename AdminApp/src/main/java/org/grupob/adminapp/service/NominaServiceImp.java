package org.grupob.adminapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.grupob.adminapp.converter.NominaConverter;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.repository.ConceptoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.LineaNominaRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NominaServiceImp implements NominaService{

    private final NominaRepository nominaRepository;
    private final NominaConverter nominaConverter;
    private final EmpleadoRepository empleadoRepository;
    private final LineaNominaRepository lineaNominaRepository;
    private final ConceptoRepository conceptoRepository;

    public NominaServiceImp(NominaRepository nominaRepository, NominaConverter nominaConverter, EmpleadoRepository empleadoRepository, LineaNominaRepository lineaNominaRepository, ConceptoRepository conceptoRepository) {
        this.nominaRepository = nominaRepository;
        this.nominaConverter = nominaConverter;
        this.empleadoRepository = empleadoRepository;
        this.lineaNominaRepository = lineaNominaRepository;
        this.conceptoRepository = conceptoRepository;
    }
    public NominaDTO devolverNominaPorId(UUID id){
        Optional<Nomina> nomina = nominaRepository.findById(id);
        if (nomina.isPresent()) {
            NominaDTO nominaDTO = nominaConverter.convierteADTO(nomina.get());
            Optional<Empleado> empleado = empleadoRepository.findById(nominaDTO.getIdEmpleado());
            empleado.ifPresent(value -> nominaDTO.setNombre(value.getNombre() + " " + value.getApellido()));
            return nominaDTO;
        } else{
            throw new EntityNotFoundException("La nómina seleccionada no existe");
        }
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
    @Transactional
    public void eliminarNomina(UUID idNomina){
        Optional<Nomina> nomina = nominaRepository.findById(idNomina);
        if (nomina.isPresent()) {

            //Verificar que la nómina no sea pasada
            YearMonth mesNomina = YearMonth.of(nomina.get().getAnio(), nomina.get().getMes());
            YearMonth mesActual = YearMonth.now();
            if (mesNomina.isBefore(mesActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una nómina de un mes anterior");
            }
            nominaRepository.delete(nomina.get());
        } else {
            throw new EntityNotFoundException("La nomina seleccionada no existe");
        }
    }

    @Transactional
    public void eliminarConcepto(UUID idNomina, UUID idConcepto) {
        Optional<Nomina> nomina = nominaRepository.findById(idNomina);
        if (nomina.isPresent()) {
            YearMonth mesNomina = YearMonth.of(nomina.get().getAnio(), nomina.get().getMes());
            YearMonth mesActual = YearMonth.now();
            if (mesNomina.isBefore(mesActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una nómina de un mes anterior");
            }
        }

        lineaNominaRepository.deleteLineaNominaByConceptoId(idConcepto);

        BigDecimal totalIngresos = lineaNominaRepository.getTotalIngresos(idNomina);
        BigDecimal totalDeducciones = lineaNominaRepository.getTotalDeducciones(idNomina);
        BigDecimal totalLiquido = totalIngresos.subtract(totalDeducciones);

        nominaRepository.updateTotalLiquido(idNomina, totalLiquido);
    }


}
