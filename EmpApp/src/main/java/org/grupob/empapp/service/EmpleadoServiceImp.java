package org.grupob.empapp.service;

import lombok.RequiredArgsConstructor;
import org.grupob.comun.exception.EmpleadoNoEncontradoException;
import org.grupob.empapp.converter.EmpleadoConverterEmp;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.comun.dto.EmpleadoSearchDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class  EmpleadoServiceImp implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoConverterEmp empleadoConverterEmp;

    // -----------------------------------
    // Métodos CRUD básicos existentes
    // -----------------------------------

    @Override
    public List<EmpleadoDTO> devuelveTodosEmpleados() {
        List<Empleado> listaempleados = empleadoRepository.findAll();
        return listaempleados.stream()
                .map(empleadoConverterEmp::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmpleadoDTO devuelveEmpleado(String id) {
        Empleado empleado = empleadoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado"));
        return empleadoConverterEmp.convertToDto(empleado);
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
    public List<EmpleadoDTO> buscarEmpleadosAvanzado(EmpleadoSearchDTO searchParams) {
        Page<EmpleadoDTO> page = buscarEmpleadosPaginados(
                searchParams,
                0, Integer.MAX_VALUE, "nombre", "asc");
        return page.getContent();
    }

    @Override
    public List<EmpleadoDTO> buscarEmpleadosPorDepartamento(String departamento) {
        List<Empleado> empleados = empleadoRepository.findByDepartamentoNombreContaining(departamento);
        return empleados.stream()
                .map(empleadoConverterEmp::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmpleadoDTO> buscarEmpleadosPorComentario(String Comentario) {
        List<Empleado> empleados = empleadoRepository.findByComentariosContainingIgnoreCase(Comentario);
        return empleados.stream()
                .map(empleadoConverterEmp::convertToDto)
                .collect(Collectors.toList());
    }

    // -----------------------------------
    // Método para búsqueda paginada y ordenada
    // -----------------------------------

    @Override
    public Page<EmpleadoDTO> buscarEmpleadosPaginados(
            EmpleadoSearchDTO searchParams,
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

        EmpleadoSearchDTO comunSearchParams = new EmpleadoSearchDTO();
        comunSearchParams.setNombre(searchParams.getNombre());
        comunSearchParams.setDepartamento(searchParams.getDepartamento());
        comunSearchParams.setComentario(searchParams.getComentario());
        comunSearchParams.setContratadosAntesDe(searchParams.getContratadosAntesDe());
        comunSearchParams.setSalarioMinimo(searchParams.getSalarioMinimo());
        comunSearchParams.setSalarioMaximo(searchParams.getSalarioMaximo());
        // Ejecutar consulta paginada
        Page<Empleado> pageEmpleados = empleadoRepository.buscarEmpleadosAvanzadoPaginado(comunSearchParams
                , pageable);

        // Convertir a DTO preservando la información de paginación
        return pageEmpleados.map(empleado -> empleadoConverterEmp.convertToDto(empleado));
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

        return empleadoConverterEmp.convertToDto(empleado);
    }

    @Override
    @Transactional
    public EmpleadoDTO quitarJefe(String empleadoId) {
        UUID empUuid = UUID.fromString(empleadoId);

        Empleado empleado = empleadoRepository.findById(empUuid)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado"));

        empleado.setJefe(null);
        empleado = empleadoRepository.save(empleado);

        return empleadoConverterEmp.convertToDto(empleado);
    }

    @Override
    public List<EmpleadoDTO> listarSubordinados(String jefeId) {
        UUID jefeUuid = UUID.fromString(jefeId);

        List<Empleado> subordinados = empleadoRepository.findByJefe_Id(jefeUuid);

        return subordinados.stream()
                .map(empleado -> empleadoConverterEmp.convertToDto(empleado))
                .collect(Collectors.toList());
    }

}
