package org.grupob.empapp.service;

import org.grupob.comun.exception.EmpleadoNoEncontradoException;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EmpleadoSearchDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class  EmpleadoServiceImp implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
//    private final EtiquetaRepository etiquetaRepository;
//    private final EmpleadoEtiquetaRepository empleadoEtiquetaRepository;
    private final EmpleadoConverter empleadoConverter;

    public EmpleadoServiceImp(
            EmpleadoRepository empleadoRepository,
//            EtiquetaRepository etiquetaRepository,
//            EmpleadoEtiquetaRepository empleadoEtiquetaRepository,
            EmpleadoConverter empleadoConverter) {

        this.empleadoRepository = empleadoRepository;
//        this.etiquetaRepository = etiquetaRepository;
//        this.empleadoEtiquetaRepository = empleadoEtiquetaRepository;
        this.empleadoConverter = empleadoConverter;
    }

    // -----------------------------------
    // Métodos CRUD básicos existentes
    // -----------------------------------

    @Override
    public List<EmpleadoDTO> devuelveTodosEmpleados() {
        List<Empleado> listaempleados = empleadoRepository.findAll();
        return listaempleados.stream()
                .map(empleado -> empleadoConverter.convertToDto(empleado))
                .collect(Collectors.toList());
    }

    @Override
    public EmpleadoDTO devuelveEmpleado(String id) {
        Empleado empleado = empleadoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado"));
        return empleadoConverter.convertToDto(empleado);
    }

    @Override
    public void eliminaEmpleadoPorId(String id) {
        UUID uuid = UUID.fromString(id);
        if (empleadoRepository.existsById(uuid)) {
            empleadoRepository.deleteById(uuid);
            return; // Para evitar lanzar la excepción si se elimina correctamente
        }
        throw new DepartamentoNoEncontradoException("El empleado no existe");
    }

    @Override
    public Empleado devuleveEmpleadoPorNombre(String nombre) {
        Optional<Empleado> empOpc = empleadoRepository.findEmpleadoByNombre(nombre);
        if (empOpc.isPresent()) {
            System.err.println(empOpc.get());
            return empOpc.get();
        } else {
            throw new DepartamentoNoEncontradoException("El empleado no existe");
        }
    }

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        if (empleado.getId() == null) {
            empleado.setId(UUID.randomUUID());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado modificarEmpleado(String id, Empleado empleado) {
        UUID uuid = UUID.fromString(id);
        if (empleadoRepository.existsById(uuid)) {
            empleado.setId(uuid);
            return empleadoRepository.save(empleado);
        }
        throw new DepartamentoNoEncontradoException("El empleado no existe");
    }

    // -----------------------------------
    // Métodos para búsqueda parametrizada
    // -----------------------------------

    @Override
    public List<EmpleadoDTO> buscarEmpleados(EmpleadoSearchDTO searchParams) {
        return buscarEmpleadosAvanzado(
                searchParams.getNombre(),
                searchParams.getDepartamento(),
                searchParams.getComentario(),
                searchParams.getContratadosAntesDe(),
                searchParams.getSalarioMinimo()
        );
    }

    @Override
    public List<EmpleadoDTO> buscarEmpleadosAvanzado(
            String nombre,
            String departamento,
            String comentario,
            LocalDate contratadosAntesDe,
            BigDecimal salarioMinimo) {

        Page<EmpleadoDTO> page = buscarEmpleadosPaginados(
                nombre, departamento, comentario, contratadosAntesDe, salarioMinimo,
                0, Integer.MAX_VALUE, "ename", "asc");

        return page.getContent();
    }

    @Override
    public List<EmpleadoDTO> buscarEmpleadosPorDepartamento(String departamento) {
        List<Empleado> empleados = empleadoRepository.findByDepartamentoNombreContaining(departamento);
        return empleados.stream()
                .map(empleado -> empleadoConverter.convertToDto(empleado))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmpleadoDTO> buscarEmpleadosPorComentario(String Comentario) {
        List<Empleado> empleados = empleadoRepository.findByComentariosContainingIgnoreCase(Comentario);
        return empleados.stream()
                .map(empleado -> empleadoConverter.convertToDto(empleado))
                .collect(Collectors.toList());
    }

    // -----------------------------------
    // Método para búsqueda paginada y ordenada
    // -----------------------------------

    @Override
    public Page<EmpleadoDTO> buscarEmpleadosPaginados(
            String nombre,
            String departamento,
            String comentario,
            LocalDate contratadosAntesDe,
            BigDecimal salarioMinimo,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        // Validar campo de ordenación (solo permitir ename o sal)
        if (sortBy == null || (!sortBy.equals("nombre") && !sortBy.equals("salario"))) {
            sortBy = "nombre"; // Valor por defecto si no es válido
        }

        // Crear objeto Sort
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        // Crear objeto Pageable
        Pageable pageable = PageRequest.of(page, size, sort);

        // Ejecutar consulta paginada
        Page<Empleado> pageEmpleados = empleadoRepository.buscarEmpleadosAvanzadoPaginado(
                nombre, departamento, comentario, contratadosAntesDe, salarioMinimo, pageable);

        // Convertir a DTO preservando la información de paginación
        return pageEmpleados.map(empleado -> empleadoConverter.convertToDto(empleado));
    }

    // -----------------------------------
    // Métodos para gestión de jefes
    // -----------------------------------

    @Override
    @Transactional
    public EmpleadoDTO asignarJefe(String empleadoId, String jefeId) {
        UUID empUuid = UUID.fromString(empleadoId);
        UUID jefeUuid = UUID.fromString(jefeId);

        // Validar que empleado y jefe existen
        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado"));

        Empleado jefe = empleadoRepository.findById(jefeUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Jefe no encontrado"));

        // Validar que no se asigne a sí mismo como jefe
        if (empUuid.equals(jefeUuid)) {
            throw new RuntimeException("Un empleado no puede ser jefe de sí mismo");
        }

        // Validar que no se cree un ciclo (un subordinado no puede ser jefe de su jefe)
        Empleado jefeActual = jefe.getJefe();
        while (jefeActual != null) {
            if (jefeActual.getId().equals(empUuid)) {
                throw new RuntimeException("No se puede crear un ciclo en la jerarquía");
            }
            jefeActual = jefeActual.getJefe();
        }

        empleado.setJefe(jefe);
        empleado = empleadoRepository.save(empleado);

        return empleadoConverter.convertToDto(empleado);
    }

    @Override
    @Transactional
    public EmpleadoDTO quitarJefe(String empleadoId) {
        UUID empUuid = UUID.fromString(empleadoId);

        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado"));

        empleado.setJefe(null);
        empleado = empleadoRepository.save(empleado);

        return empleadoConverter.convertToDto(empleado);
    }

    @Override
    public List<EmpleadoDTO> listarSubordinados(String jefeId) {
        UUID jefeUuid = UUID.fromString(jefeId);

        List<Empleado> subordinados = empleadoRepository.findByJefe_Id(jefeUuid);

        return subordinados.stream()
                .map(empleado -> empleadoConverter.convertToDto(empleado))
                .collect(Collectors.toList());
    }

    // -----------------------------------
    // Métodos para gestión de etiquetas
    // -----------------------------------

//    @Override
//    @Transactional
//    public EmpleadoDTO asignarEtiqueta(String empleadoId, String etiquetaId) {
//        UUID empUuid = UUID.fromString(empleadoId);
//        UUID etiqUuid = UUID.fromString(etiquetaId);
//
//        // Verificar si la relación ya existe usando el repositorio
//        if (empleadoEtiquetaRepository.existsByEmpleadoIdAndEtiquetaId(empUuid, etiqUuid)) {
//            // La relación ya existe, simplemente devolver el empleado
//            Empleado empleado = empleadoRepository.findById(empUuid)
//                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
//            return convertToDto(empleado);
//        }
//
//        // Cargar entidades
//        Empleado empleado = empleadoRepository.findById(empUuid)
//                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
//        Etiqueta etiqueta = etiquetaRepository.findById(etiqUuid)
//                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
//
//        // Crear la relación
//        EmpleadoEtiquetaId id = new EmpleadoEtiquetaId(empUuid, etiqUuid);
//        EmpleadoEtiqueta empleadoEtiqueta = new EmpleadoEtiqueta();
//        empleadoEtiqueta.setId(id);
//        empleadoEtiqueta.setEmpleado(empleado);
//        empleadoEtiqueta.setEtiqueta(etiqueta);
//        empleadoEtiqueta.setFechaAsignacion(LocalDate.now());
//
//        // OPCIÓN 1: Solo guardar en el repositorio sin añadir a la colección
//        empleadoEtiquetaRepository.save(empleadoEtiqueta);
//
//        // Recargar el empleado para obtener la colección actualizada
//        empleado = empleadoRepository.findById(empUuid)
//                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
//
//        return convertToDto(empleado);
//    }
//
//    @Override
//    @Transactional
//    public EmpleadoDTO quitarEtiqueta(String empleadoId, String etiquetaId) {
//        UUID empUuid = UUID.fromString(empleadoId);
//        UUID etiqUuid = UUID.fromString(etiquetaId);
//
//        // Obtener el empleado
//        Empleado empleado = empleadoRepository.findById(empUuid)
//                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado"));
//
//        // Eliminar la relación empleado-etiqueta
//        empleadoEtiquetaRepository.deleteByEmpleadoIdAndEtiquetaId(empUuid, etiqUuid);
//
//        // Refrescar el empleado desde la base de datos
//        empleado = empleadoRepository.findById(empUuid).orElseThrow();
//
//        return convertToDto(empleado);
//    }
//
//    @Override
//    public List<EmpleadoDTO> buscarPorEtiqueta(String etiquetaId) {
//        UUID etiqUuid = UUID.fromString(etiquetaId);
//
//        // Buscar empleados que tengan la etiqueta especificada
//        List<Empleado> empleados = empleadoRepository.findByEtiquetaId(etiqUuid);
//
//        return empleados.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }



}
