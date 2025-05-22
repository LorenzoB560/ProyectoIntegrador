package org.grupob.empapp.service;

import org.grupob.empapp.converter.EmpleadoConverterEmp;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Etiqueta;
import org.grupob.comun.exception.DepartamentoNoEncontradoException; // Asegúrate que esta excepción exista y sea apropiada
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.EtiquetaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate; // Necesario para inicializar colecciones lazy

import jakarta.persistence.EntityManager; // Necesario para flush explícito
import jakarta.persistence.PersistenceContext; // Necesario para inyectar EntityManager

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional // Aplica transacción por defecto a todos los métodos públicos
public class EtiquetaServiceImp implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ModelMapper modelMapper; // Para mapear listas de DTOs o sub-objetos
    private final EmpleadoConverterEmp empleadoConverterEmp; // Para convertir el resultado final

    @PersistenceContext // Inyectar EntityManager
    private EntityManager entityManager;

    public EtiquetaServiceImp(EtiquetaRepository etiquetaRepository,
                              EmpleadoRepository empleadoRepository,
                              ModelMapper modelMapper,
                              EmpleadoConverterEmp empleadoConverterEmp) {
        this.etiquetaRepository = etiquetaRepository;
        this.empleadoRepository = empleadoRepository;
        this.modelMapper = modelMapper;
        this.empleadoConverterEmp = empleadoConverterEmp;
    }

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

    // Método para sugerencias (usa el método del repositorio)
    @Transactional(readOnly = true)
    public List<EtiquetaDTO> buscarEtiquetasPorPrefijo(UUID jefeId, String prefijo) {
        List<Etiqueta> etiquetas = etiquetaRepository.findByCreadorIdAndNombreStartingWithIgnoreCaseOrderByNombreAsc(jefeId, prefijo);
        Type listType = new TypeToken<List<EtiquetaDTO>>() {}.getType();
        return modelMapper.map(etiquetas, listType);
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
        return empleadoConverterEmp.convertToDto(empleado);
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
            System.err.println(">>> [DEBUG EliminarInd] ERROR: El empleado " + empleadoId + " NO tiene jefe asignado.");
            throw new IllegalArgumentException("El empleado no tiene jefe asignado.");
        }
        if (!empleado.getJefe().getId().equals(jefeUuid)) {
            System.err.println(">>> [DEBUG EliminarInd] ERROR: El jefe " + jefeId + " NO es el jefe ("+ empleado.getJefe().getId() +") del empleado " + empleadoId);
            throw new IllegalArgumentException("El empleado no es subordinado del jefe especificado.");
        }
        System.out.println(">>> [DEBUG EliminarInd] Validación de jefe OK.");


        // Eliminar de ambos lados
        System.out.println(">>> [DEBUG EliminarInd] Modificando colecciones...");
        boolean removedEmp = false;
        boolean removedEtiq = false;

        if (empleado.getEtiquetas() != null) { // Importante chequear nulidad
            System.out.println(">>> [DEBUG EliminarInd] Tamaño etiquetas ANTES para empleado " + empleadoId + ": " + empleado.getEtiquetas().size());
            removedEmp = empleado.getEtiquetas().remove(etiqueta); // Lado propietario
            System.out.println(">>> [DEBUG EliminarInd] Tamaño etiquetas DESPUÉS para empleado " + empleadoId + ": " + empleado.getEtiquetas().size() + " (Eliminado: " + removedEmp + ")");
        } else {
            System.out.println(">>> [DEBUG EliminarInd] El empleado " + empleadoId + " tenía colección de etiquetas null.");
        }

        if (etiqueta.getEmpleados() != null) { // Importante chequear nulidad
            System.out.println(">>> [DEBUG EliminarInd] Tamaño empleados ANTES para etiqueta " + etiquetaId + ": " + etiqueta.getEmpleados().size());
            removedEtiq = etiqueta.getEmpleados().remove(empleado); // Lado inverso (buena práctica)
            System.out.println(">>> [DEBUG EliminarInd] Tamaño empleados DESPUÉS para etiqueta " + etiquetaId + ": " + etiqueta.getEmpleados().size() + " (Eliminado: " + removedEtiq + ")");
        } else {
            System.out.println(">>> [DEBUG EliminarInd] La etiqueta " + etiquetaId + " tenía colección de empleados null.");
        }


        // --- CAMBIO IMPORTANTE AQUÍ ---
        if (removedEmp) { // Solo necesitamos guardar si la colección del lado propietario (Empleado) cambió
            System.out.println(">>> [DEBUG EliminarInd] Asociación eliminada del empleado. Guardando empleado y haciendo flush...");
            try {
                // Guardar explícitamente la entidad dueña tras modificar la colección
                empleadoRepository.save(empleado);
                System.out.println(">>> [DEBUG EliminarInd] save(empleado) ejecutado.");

                entityManager.flush(); // Forzar SQL ahora para detectar errores de BD antes del commit
                System.out.println(">>> [DEBUG EliminarInd] Flush exitoso.");
            } catch (Exception e) {
                System.err.println("!!! EXCEPCIÓN durante save/flush en eliminarEtiquetaDeEmpleado !!!");
                e.printStackTrace();
                // Relanzar para que la transacción haga rollback y el controlador devuelva error 500
                throw new RuntimeException("Error durante la persistencia al eliminar etiqueta: " + e.getMessage(), e);
            }
        } else if (removedEtiq) {
            System.out.println(">>> [DEBUG EliminarInd] Asociación eliminada solo del lado de la etiqueta (no propietario). No se requiere save explícito del empleado.");
            // Podrías guardar la etiqueta si tuviera otros cambios, pero no es necesario para la relación ManyToMany aquí.
        }
        else {
            System.out.println("Advertencia: La etiqueta " + etiquetaId + " no estaba asignada al empleado " + empleadoId + " (o las colecciones eran null).");
        }
        // --- FIN DEL CAMBIO ---

        System.out.println(">>> [DEBUG EliminarInd] Fin del método (antes de commit/rollback).");
        // Se devuelve el DTO del empleado (potencialmente sin la etiqueta si se guardó bien)
        return empleadoConverterEmp.convertToDto(empleado);
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
                .map(empleadoConverterEmp::convertToDto)
                .collect(Collectors.toList());
    }

    // El método asignarEtiquetasMasivo se puede dejar comentado o eliminar si no se usa
    /*
    @Override
    @Transactional
    public void asignarEtiquetasMasivo(String jefeId, List<String> empleadoIds, List<String> etiquetaIds) {
        // ... Implementación anterior si decides mantenerla ...
    }
    */
}