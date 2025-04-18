package org.grupob.empapp.component;

import org.grupob.empapp.entity.Departamento;
import org.grupob.empapp.repository.DepartamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CargaDatosDepartamento implements CommandLineRunner {

    DepartamentoRepository departamentoRepository;

    public CargaDatosDepartamento(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (departamentoRepository.count() == 0) {
            departamentoRepository.save(new Departamento("Informática", "10"));
            departamentoRepository.save(new Departamento("RRHH", "20"));
            departamentoRepository.save(new Departamento("Marketing", "30"));
            departamentoRepository.save(new Departamento("Contabilidad", "40"));
        }
    }
}
