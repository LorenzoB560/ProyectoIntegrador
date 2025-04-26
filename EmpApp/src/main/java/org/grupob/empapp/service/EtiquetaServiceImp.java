package org.grupob.empapp.service;

import org.grupob.empapp.converter.EmpleadoConverter; // Necesario para devolver EmpleadoDTO
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Etiqueta;
import org.grupob.comun.exception.DepartamentoNoEncontradoException; // O una excepción más específica
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.EtiquetaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtiquetaServiceImp implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;
    private final EmpleadoRepository empleadoRepository; // Necesario para buscar/validar empleados y jefes
    private final ModelMapper modelMapper;
    private final EmpleadoConverter empleadoConverter; // Necesario para los métodos que devuelven EmpleadoDTO

    public EtiquetaServiceImp(EtiquetaRepository etiquetaRepository,
                              EmpleadoRepository empleadoRepository,
                              ModelMapper modelMapper,
                              EmpleadoConverter empleadoConverter) {
        this.etiquetaRepository = etiquetaRepository;
        this.empleadoRepository = empleadoRepository;
        this.modelMapper = modelMapper;
        this.empleadoConverter = empleadoConverter;
    }

    @Override
    public List<EtiquetaDTO> listarEtiquetasPorJefe(String jefeId) {
        UUID jefeUuid = UUID.fromString(jefeId);
        List<Etiqueta> etiquetas = etiquetaRepository.findByCreador_IdOrderByNombreAsc(jefeUuid);
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
    }

    @Override
    public List<EtiquetaDTO> listarTodasEtiquetasGlobales() {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll(Sort.by("nombre"));
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
    }

    @Override
    @Transactional
    public void asignarEtiquetasMasivo(String jefeId, List<String> empleadoIds, List<String> etiquetaIds) {
        UUID jefeUuid = UUID.fromString(jefeId);

        if (!empleadoRepository.existsById(jefeUuid)) {
            throw new DepartamentoNoEncontradoException("Jefe no encontrado con ID: " + jefeId);
        }

        List<UUID> empUuids = empleadoIds.stream().map(UUID::fromString).collect(Collectors.toList());
        List<UUID> etiqUuids = etiquetaIds.stream().map(UUID::fromString).collect(Collectors.toList());

        List<Empleado> empleados = empleadoRepository.findAllById(empUuids);
        List<Etiqueta> etiquetas = etiquetaRepository.findAllById(etiqUuids);

        if (empleados.size() != empUuids.size()) {
            // Podríamos ser más específicos sobre qué empleado falta
            throw new DepartamentoNoEncontradoException("Uno o más empleados no fueron encontrados.");
        }
        if (etiquetas.size() != etiqUuids.size()) {
            // Podríamos ser más específicos sobre qué etiqueta falta
            throw new DepartamentoNoEncontradoException("Una o más etiquetas no fueron encontradas.");
        }

        for (Empleado emp : empleados) {
            if (emp.getJefe() == null || !emp.getJefe().getId().equals(jefeUuid)) {
                throw new IllegalArgumentException("El empleado con ID " + emp.getId() + " no es subordinado del jefe con ID " + jefeId);
            }
            // Validar si las etiquetas pertenecen al jefe (si esa es la regla de negocio)
            /*
            for (Etiqueta et : etiquetas) {
                if (!et.getCreador().getId().equals(jefeUuid)) {
                     throw new IllegalArgumentException("La etiqueta '" + et.getNombre() + "' no pertenece al jefe.");
                }
            }*/
            etiquetas.forEach(emp::addEtiqueta);
        }

        empleadoRepository.saveAll(empleados);
    }

    @Override
    @Transactional
    public Etiqueta buscarOCrearEtiqueta(String nombreEtiqueta, UUID jefeId) {
        return etiquetaRepository.findByNombreIgnoreCaseAndCreador_Id(nombreEtiqueta, jefeId)
                .orElseGet(() -> {
                    Empleado jefe = empleadoRepository.findById(jefeId)
                            .orElseThrow(() -> new DepartamentoNoEncontradoException("Jefe no encontrado con ID: " + jefeId));
                    Etiqueta nuevaEtiqueta = new Etiqueta(nombreEtiqueta.trim(), jefe); // Guardar sin espacios extra
                    return etiquetaRepository.save(nuevaEtiqueta);
                });
    }

    @Override
    @Transactional
    public EmpleadoDTO asignarEtiquetaSimple(String empleadoId, String nombreEtiqueta, String jefeId) {
        UUID empUuid = UUID.fromString(empleadoId);
        UUID jefeUuid = UUID.fromString(jefeId);

        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado con ID: " + empleadoId));

        if (empleado.getJefe() == null || !empleado.getJefe().getId().equals(jefeUuid)) {
            throw new IllegalArgumentException("El empleado no es subordinado del jefe especificado.");
        }

        Etiqueta etiqueta = buscarOCrearEtiqueta(nombreEtiqueta, jefeUuid);
        empleado.addEtiqueta(etiqueta);
        Empleado empleadoGuardado = empleadoRepository.save(empleado);
        return empleadoConverter.convertToDto(empleadoGuardado);
    }

    @Override
    @Transactional
    public EmpleadoDTO eliminarEtiquetaDeEmpleado(String empleadoId, String etiquetaId, String jefeId) {
        UUID empUuid = UUID.fromString(empleadoId);
        UUID etiqUuid = UUID.fromString(etiquetaId);
        UUID jefeUuid = UUID.fromString(jefeId);

        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado con ID: " + empleadoId));
        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Etiqueta no encontrada con ID: " + etiquetaId));

        if (empleado.getJefe() == null || !empleado.getJefe().getId().equals(jefeUuid)) {
            throw new IllegalArgumentException("El empleado no es subordinado del jefe especificado.");
        }

        // Opcional: Validar si la etiqueta pertenece al jefe
        // if (!etiqueta.getCreador().getId().equals(jefeUuid)) { ... }

        if (empleado.getEtiquetas().contains(etiqueta)) {
            empleado.removeEtiqueta(etiqueta);
            empleadoRepository.save(empleado);
        } else {
            System.out.println("Advertencia: La etiqueta " + etiquetaId + " no estaba asignada al empleado " + empleadoId);
        }

        // Devolvemos el DTO del empleado actualizado (puede o no haber cambiado)
        return empleadoConverter.convertToDto(empleado);
    }
}