package org.grupob.adminapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Etiqueta;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.EtiquetaRepository;
import org.grupob.adminapp.converter.EmpleadoConverterAdmin;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.adminapp.dto.EtiquetaDTO;
import org.grupob.adminapp.service.EtiquetaService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional // Aplica transacción por defecto a todos los métodos públicos
@RequiredArgsConstructor
public class EtiquetaServiceImp implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ModelMapper modelMapper;
    private final EmpleadoConverterAdmin empleadoConverter;

    @PersistenceContext // Inyectar EntityManager
    private EntityManager entityManager;


    @Override
    @Transactional(readOnly = true)
    public List<EtiquetaDTO> listarEtiquetasPorJefe(String jefeId) {
        UUID jefeUuid = UUID.fromString(jefeId);
        if (!empleadoRepository.existsById(jefeUuid)) {
            throw new DepartamentoNoEncontradoException("Jefe no encontrado con ID: " + jefeId);
        }
        List<Etiqueta> etiquetas = etiquetaRepository.findByCreador_IdOrderByNombreAsc(jefeUuid);
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtiquetaDTO> listarTodasEtiquetasGlobales() {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll(Sort.by("nombre"));
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
    }

    @Override
    @Transactional // Asegura que la creación sea transaccional
    public Etiqueta buscarOCrearEtiqueta(String nombreEtiqueta, UUID jefeId) {
        return etiquetaRepository.findByNombreIgnoreCaseAndCreador_Id(nombreEtiqueta.trim(), jefeId)
                .orElseGet(() -> {
                    Empleado jefe = empleadoRepository.findById(jefeId)
                            .orElseThrow(() -> new DepartamentoNoEncontradoException("Jefe no encontrado con ID: " + jefeId + " al intentar crear etiqueta."));
                    Etiqueta nuevaEtiqueta = new Etiqueta(nombreEtiqueta.trim(), jefe);
                    System.out.println(">>> [DEBUG CrearEtiq] Creando nueva etiqueta: " + nombreEtiqueta + " para jefe: " + jefeId);
                    return etiquetaRepository.save(nuevaEtiqueta);
                });
    }

    @Override
    @Transactional // Transacción de escritura
    public EmpleadoDTO asignarEtiquetaExistente(String empleadoId, String etiquetaId, String jefeId) {
        UUID empUuid = UUID.fromString(empleadoId);
        UUID etiqUuid = UUID.fromString(etiquetaId);
        UUID jefeUuid = UUID.fromString(jefeId);

        System.out.println(">>> [DEBUG AsignarInd] Buscando Empleado: " + empUuid);
        // 1. Cargar explícitamente DENTRO de esta transacción
        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado: " + empleadoId));
        System.out.println(">>> [DEBUG AsignarInd] Buscando Etiqueta: " + etiqUuid);
        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Etiqueta no encontrada: " + etiquetaId));

        // 2. Validaciones
        System.out.println(">>> [DEBUG AsignarInd] Validando jefe...");
        if (empleado.getJefe() == null) {
            // Forzar inicialización si es LAZY y sospechas que puede ser un proxy no inicializado
            // Hibernate.initialize(empleado.getJefe());
            // if (empleado.getJefe() == null) { // Re-verificar después de inicializar
            throw new IllegalArgumentException("El empleado no tiene jefe asignado.");
            // }
        }
        if (!empleado.getJefe().getId().equals(jefeUuid)) {
            throw new IllegalArgumentException("El empleado no es subordinado del jefe especificado.");
        }
        // Opcional: Validar si el jefe puede usar esta etiqueta (ej. si debe ser el creador)
        // if(!etiqueta.getCreador().getId().equals(jefeUuid)) { ... }

        // 3. Modificar ambos lados (asegurando inicialización de colecciones)
        System.out.println(">>> [DEBUG AsignarInd] Modificando colecciones...");
        if (empleado.getEtiquetas() == null) empleado.setEtiquetas(new HashSet<>());
        if (etiqueta.getEmpleados() == null) etiqueta.setEmpleados(new HashSet<>());

        boolean changed = empleado.getEtiquetas().add(etiqueta); // Lado propietario
        etiqueta.getEmpleados().add(empleado); // Lado inverso

        if (changed) {
            System.out.println(">>> [DEBUG AsignarInd] Asociación añadida en memoria. Intentando flush...");
            // 4. Forzar Flush (principalmente para diagnóstico)
            try {
                // save(empleado) no debería ser estrictamente necesario si la entidad está gestionada
                // y se modificó, pero no hace daño (devuelve la instancia gestionada).
                // empleadoRepository.save(empleado);
                entityManager.flush(); // Forzar sincronización AHORA
                System.out.println(">>> [DEBUG AsignarInd] Flush exitoso.");
            } catch (Exception e) {
                System.err.println("!!! EXCEPCIÓN durante flush en asignarEtiquetaExistente !!!");
                e.printStackTrace();
                throw new RuntimeException("Error durante flush en asignarEtiquetaExistente: " + e.getMessage(), e);
            }
        } else {
            System.out.println(">>> [DEBUG AsignarInd] La asociación ya existía.");
        }

        System.out.println(">>> [DEBUG AsignarInd] Fin del método (antes de commit/rollback).");
        // 5. Convertir y devolver (usa el converter manual seguro)
        return empleadoConverter.convertToDto(empleado);
    }

    @Override
    @Transactional // Transacción de escritura
    public EmpleadoDTO eliminarEtiquetaDeEmpleado(String empleadoId, String etiquetaId, String jefeId) {
        UUID empUuid = UUID.fromString(empleadoId);
        UUID etiqUuid = UUID.fromString(etiquetaId);
        UUID jefeUuid = UUID.fromString(jefeId);

        System.out.println(">>> [DEBUG EliminarInd] Buscando Empleado: " + empUuid);
        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado: " + empleadoId));
        System.out.println(">>> [DEBUG EliminarInd] Buscando Etiqueta: " + etiqUuid);
        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Etiqueta no encontrada: " + etiquetaId));

        // Validaciones
        System.out.println(">>> [DEBUG EliminarInd] Validando jefe...");
        if (empleado.getJefe() == null) {
            throw new IllegalArgumentException("El empleado no tiene jefe asignado.");
        }
        if (!empleado.getJefe().getId().equals(jefeUuid)) {
            throw new IllegalArgumentException("El empleado no es subordinado del jefe especificado.");
        }

        // Eliminar de ambos lados
        System.out.println(">>> [DEBUG EliminarInd] Modificando colecciones...");
        boolean removedEmp = false;
        boolean removedEtiq = false;

        if (empleado.getEtiquetas() != null) { // Importante chequear nulidad antes de remover
            removedEmp = empleado.getEtiquetas().remove(etiqueta);
        }
        if (etiqueta.getEmpleados() != null) { // Importante chequear nulidad antes de remover
            removedEtiq = etiqueta.getEmpleados().remove(empleado);
        }

        if (removedEmp || removedEtiq) { // Si se quitó de CUALQUIER lado
            System.out.println(">>> [DEBUG EliminarInd] Asociación eliminada en memoria. Intentando flush...");
            try {
                // El commit debería encargarse. Flush es para diagnóstico.
                entityManager.flush();
                System.out.println(">>> [DEBUG EliminarInd] Flush exitoso.");
            } catch (Exception e) {
                System.err.println("!!! EXCEPCIÓN durante flush en eliminarEtiquetaDeEmpleado !!!");
                e.printStackTrace();
                throw new RuntimeException("Error durante flush en eliminarEtiquetaDeEmpleado: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Advertencia: La etiqueta " + etiquetaId + " no estaba asignada al empleado " + empleadoId + " o viceversa.");
        }

        System.out.println(">>> [DEBUG EliminarInd] Fin del método (antes de commit/rollback).");
        return empleadoConverter.convertToDto(empleado);
    }


    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDTO> buscarEmpleadosPorEtiqueta(String etiquetaId) {
        UUID etiqUuid = UUID.fromString(etiquetaId);
        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Etiqueta no encontrada con ID: " + etiquetaId));

        // Forzar inicialización de la colección DENTRO de la transacción readOnly
        Set<Empleado> empleados = etiqueta.getEmpleados();
        System.out.println(">>> [DEBUG BuscarPorEtiq] Intentando inicializar colección empleados para Etiqueta: " + etiqUuid);
        Hibernate.initialize(empleados); // Esencial si la relación es LAZY
        System.out.println(">>> [DEBUG BuscarPorEtiq] Colección inicializada. Tamaño: " + (empleados != null ? empleados.size() : "null"));


        if (empleados == null || empleados.isEmpty()) {
            return new ArrayList<>();
        }

        // Convertir a DTOs
        return empleados.stream()
                .map(empleadoConverter::convertToDto)
                .collect(Collectors.toList());
    }
}