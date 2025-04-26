package org.grupob.empapp.service;

import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Etiqueta;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.EtiquetaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate; // Importar Hibernate si necesitas verificar inicialización en otros métodos
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtiquetaServiceImp implements EtiquetaService {
    private final EtiquetaRepository etiquetaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ModelMapper modelMapper;
    private final EmpleadoConverter empleadoConverter;

    public EtiquetaServiceImp(EtiquetaRepository etiquetaRepository,
                              EmpleadoRepository empleadoRepository,
                              ModelMapper modelMapper,
                              EmpleadoConverter empleadoConverter) {
        // ... (asignaciones del constructor) ...
        this.etiquetaRepository = etiquetaRepository;
        this.empleadoRepository = empleadoRepository;
        this.modelMapper = modelMapper;
        this.empleadoConverter = empleadoConverter;
    }

    @Override
    @Transactional(readOnly = true) // Este método solo lee
    public List<EtiquetaDTO> listarEtiquetasPorJefe(String jefeId) {
        // ... (código existente) ...
        UUID jefeUuid = UUID.fromString(jefeId);
        if (!empleadoRepository.existsById(jefeUuid)) {
            throw new DepartamentoNoEncontradoException("Jefe no encontrado con ID: " + jefeId);
        }
        List<Etiqueta> etiquetas = etiquetaRepository.findByCreador_IdOrderByNombreAsc(jefeUuid);
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
    }

    @Override
    @Transactional(readOnly = true) // Este método solo lee
    public List<EtiquetaDTO> listarTodasEtiquetasGlobales() {
        // ... (código existente) ...
        List<Etiqueta> etiquetas = etiquetaRepository.findAll(Sort.by("nombre"));
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
    }

    // Eliminamos el método asignarEtiquetasMasivo

    @Override
    @Transactional // Método de escritura
    public Etiqueta buscarOCrearEtiqueta(String nombreEtiqueta, UUID jefeId) {
        // ... (código existente, útil si se necesita crear etiquetas desde otro lugar) ...
        return etiquetaRepository.findByNombreIgnoreCaseAndCreador_Id(nombreEtiqueta.trim(), jefeId)
                .orElseGet(() -> {
                    Empleado jefe = empleadoRepository.findById(jefeId)
                            .orElseThrow(() -> new DepartamentoNoEncontradoException("Jefe no encontrado con ID: " + jefeId));
                    Etiqueta nuevaEtiqueta = new Etiqueta(nombreEtiqueta.trim(), jefe);
                    return etiquetaRepository.save(nuevaEtiqueta);
                });
    }

    @Override
    @Transactional // Método de escritura
    public EmpleadoDTO asignarEtiquetaExistente(String empleadoId, String etiquetaId, String jefeId) {
        UUID empUuid = UUID.fromString(empleadoId);
        UUID etiqUuid = UUID.fromString(etiquetaId);
        UUID jefeUuid = UUID.fromString(jefeId);

        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado con ID: " + empleadoId));
        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Etiqueta no encontrada con ID: " + etiquetaId));

        // Validación: ¿Es subordinado?
        if (empleado.getJefe() == null || !empleado.getJefe().getId().equals(jefeUuid)) {
            throw new IllegalArgumentException("El empleado no es subordinado del jefe especificado.");
        }
        // Validación: ¿El jefe puede usar esta etiqueta? (Depende de reglas de negocio, ej. si solo puede usar las que él creó)
        // if(!etiqueta.getCreador().getId().equals(jefeUuid)) {
        //     throw new IllegalArgumentException("El jefe no tiene permiso para asignar esta etiqueta.");
        // }


        if (empleado.getEtiquetas() == null) {
            empleado.setEtiquetas(new HashSet<>());
        }
        boolean added = empleado.getEtiquetas().add(etiqueta);

        if (added) {
            // Guardamos porque hubo un cambio en la colección del empleado
            empleadoRepository.save(empleado);
            System.out.println("Etiqueta " + etiquetaId + " asignada a empleado " + empleadoId);
        } else {
            System.out.println("Etiqueta " + etiquetaId + " ya estaba asignada a empleado " + empleadoId);
        }

        // Devolvemos el DTO actualizado (usando el converter manual seguro)
        return empleadoConverter.convertToDto(empleado);
    }

    @Override
    @Transactional // Método de escritura
    public EmpleadoDTO eliminarEtiquetaDeEmpleado(String empleadoId, String etiquetaId, String jefeId) {
        // La implementación existente ya funciona para esto
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
        // Opcional: Validar si el jefe puede eliminar esta etiqueta

        boolean removed = false;
        if (empleado.getEtiquetas() != null) {
            removed = empleado.getEtiquetas().remove(etiqueta);
        }

        if (removed) {
            empleadoRepository.save(empleado);
            System.out.println("Etiqueta " + etiquetaId + " eliminada del empleado " + empleadoId);
        } else {
            System.out.println("Advertencia: La etiqueta " + etiquetaId + " no estaba asignada al empleado " + empleadoId);
        }

        return empleadoConverter.convertToDto(empleado);
    }

    @Override
    @Transactional(readOnly = true) // Método de solo lectura
    public List<EmpleadoDTO> buscarEmpleadosPorEtiqueta(String etiquetaId) {
        UUID etiqUuid = UUID.fromString(etiquetaId);

        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Etiqueta no encontrada con ID: " + etiquetaId));

        // Necesitamos inicializar la colección de empleados asociada a la etiqueta
        Set<Empleado> empleados = etiqueta.getEmpleados();
        Hibernate.initialize(empleados); // Forzar carga si es LAZY

        if (empleados == null || empleados.isEmpty()) {
            return new ArrayList<>(); // Devolver lista vacía si no hay empleados
        }

        // Convertir las entidades Empleado a EmpleadoDTO
        return empleados.stream()
                .map(empleadoConverter::convertToDto) // Usar el converter manual seguro
                .collect(Collectors.toList());
    }
}