package org.grupob.comun.component;

import org.grupob.comun.entity.maestras.Genero;
import org.grupob.comun.entity.maestras.Idioma;
import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.grupob.comun.repository.maestras.IdiomaRepository;
import org.grupob.comun.repository.maestras.MotivoBloqueoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CargaDatosMaestros implements CommandLineRunner {

    //    @Autowired
    private final GeneroRepository generoRepo;
    //    @Autowired
    private final IdiomaRepository idiomaRepo;
    //    @Autowired
    private final MotivoBloqueoRepository motivoRepo;

    public CargaDatosMaestros(GeneroRepository generoRepo, IdiomaRepository idiomaRepo, MotivoBloqueoRepository motivoRepo) {
        this.generoRepo = generoRepo;
        this.idiomaRepo = idiomaRepo;
        this.motivoRepo = motivoRepo;
    }

    @Override
    public void run(String... args) throws Exception {
//        if (generoRepo.count() == 0) {
//            generoRepo.save(new Genero(1L, "Masculino"));
//            generoRepo.save(new Genero(2L, "Femenino"));
//            generoRepo.save(new Genero(3L, "Otro"));
//        }
        if (idiomaRepo.count() == 0) {
            idiomaRepo.save(new Idioma(1L, "es", "Español"));
            idiomaRepo.save(new Idioma(2L, "en", "Inglés"));
        }
        if (motivoRepo.count() == 0) {
            motivoRepo.save(new MotivoBloqueo(1L, "Intentos fallidos", 15));
            motivoRepo.save(new MotivoBloqueo(2L, "Falta leve", 30));
            motivoRepo.save(new MotivoBloqueo(3L, "Falta media", 45));
            motivoRepo.save(new MotivoBloqueo(4L, "Falta grave", 60));
        }
        System.out.println("DATOS MAESTROS CARGADOS CORRECTAMENTE");
    }
}
