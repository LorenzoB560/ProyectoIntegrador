package org.grupob.empapp.component;

import org.grupob.empapp.entity.Administrador;
import org.grupob.empapp.entity.UsuarioEmpleado;
import org.grupob.empapp.repository.AdministradorRepository;
import org.grupob.empapp.repository.UsuarioEmpleadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CargaDatosUsuarios implements CommandLineRunner {

    //    @Autowired
    private final AdministradorRepository adminRepo;
    //    @Autowired
    private final UsuarioEmpleadoRepository usuarioEmpleadoRepo;

    public CargaDatosUsuarios(AdministradorRepository adminRepo, UsuarioEmpleadoRepository usuarioEmpleadoRepo) {
        this.adminRepo = adminRepo;
        this.usuarioEmpleadoRepo = usuarioEmpleadoRepo;
    }


    @Override
    public void run(String... args) throws Exception {
        if (adminRepo.count() == 0) {
            adminRepo.save(new Administrador("admin1@gmail.com", "admin123"));
            adminRepo.save(new Administrador("admin2@gmail.com", "admin123"));
        }

        if (usuarioEmpleadoRepo.count() != 0) {
            usuarioEmpleadoRepo.save(new UsuarioEmpleado("emp1@gmail.com", "empleado"));
            usuarioEmpleadoRepo.save(new UsuarioEmpleado("emp2@gmail.com", "empleado"));
        }

        System.out.println("DATOS USUARIOS DE PRUEBA CARGADOS CORRECTAMENTE");
    }
}
