package org.grupob.adminapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.dto.ModificacionEmpleadoDTO;
import org.grupob.comun.entity.*;
import org.grupob.comun.entity.maestras.TipoTarjetaCredito;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.exception.EmpleadoNoEncontradoException;
import org.grupob.comun.exception.UsuarioNoEncontradoException;
import org.grupob.comun.repository.*;
import org.grupob.adminapp.converter.EmpleadoConverterAdmin;
import org.grupob.adminapp.dto.EmpleadoDTO;
import org.grupob.comun.dto.EmpleadoSearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public  class  EmpleadoServiceImp implements EmpleadoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImp.class);
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoConverterAdmin empleadoConverter;
    private final UsuarioEmpleadoRepository usuarioEmpleadoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EntidadBancariaRepository entidadBancariaRepository;
    private final TipoTarjetaRepository tipoTarjetaRepository;

    private final List<String> empleadosDesbloqueadosRecientemente = Collections.synchronizedList(new ArrayList<>());

    // -----------------------------------
    // Métodos CRUD básicos existentes
    // -----------------------------------

    @Override
    public List<EmpleadoDTO> devuelveTodosEmpleadosActivos() {
        List<Empleado> empleadosActivos = empleadoRepository.findByActivoTrue(); // Usando el nuevo método
        return empleadosActivos.stream()
                .map(empleadoConverter::convertToDto)
                .collect(Collectors.toList());
    }

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
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Empleado no encontrado"));
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
    public Empleado modificarEmpleado(String id, ModificacionEmpleadoDTO modificacionEmpleadoDTO) {
        Optional<Empleado> empleado = empleadoRepository.findById(UUID.fromString(id));
//        Empleado empleado = empleadoConverterAdmin.convertirAEntidadDesdeModificacion(modificacionEmpleadoDTO);
        UUID uuid = UUID.fromString(id);
        Optional<UsuarioEmpleado> usuarioEmpleado = usuarioEmpleadoRepository.findById(uuid);
        if (empleado.isPresent()) {
            Empleado e = empleado.get();
            e.setId(uuid);
            e.setActivo(true);
            usuarioEmpleado.ifPresent(e::setUsuario);

            System.err.println("EMPLEADO: " + e);
            e.setNombre(!modificacionEmpleadoDTO.getNombre().isEmpty() ? modificacionEmpleadoDTO.getNombre() : e.getNombre());
            e.setApellido(!modificacionEmpleadoDTO.getApellido().isEmpty() ? modificacionEmpleadoDTO.getApellido() : e.getApellido());
            e.setFechaNacimiento(modificacionEmpleadoDTO.getFechaNacimiento() != null ? modificacionEmpleadoDTO.getFechaNacimiento() : e.getFechaNacimiento());

            return empleadoRepository.save(e);
        }
        throw new EmpleadoNoEncontradoException("El empleado no existe");
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
        return pageEmpleados.map(empleadoConverter::convertToDto);
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

    @Override
    @Transactional
    public EmpleadoDTO desactivarEmpleado(String id) {
        UUID empleadoUuid = UUID.fromString(id);
        Empleado empleado = empleadoRepository.findById(empleadoUuid)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

        if (!empleado.isActivo()) {
            // Podrías lanzar una excepción personalizada o manejarlo como prefieras
            throw new IllegalStateException("El empleado ya se encuentra desactivado.");
        }

        empleado.setActivo(false);
        // Opcional: empleado.getPeriodo().setFechaFin(LocalDate.now());
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        return empleadoConverter.convertToDto(empleadoActualizado);
    }

    @Override
    @Transactional
    public EmpleadoDTO activarEmpleado(String id) {
        UUID empleadoUuid = UUID.fromString(id);
        Empleado empleado = empleadoRepository.findById(empleadoUuid)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

        if (empleado.isActivo()) {
            throw new IllegalStateException("El empleado ya se encuentra activo.");
        }
        empleado.setActivo(true);
        // Opcional: empleado.getPeriodo().setFechaFin(null);
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        return empleadoConverter.convertToDto(empleadoActualizado);
    }

    public List<EmpleadoDTO> devuelveTodosEmpleadosInactivos() {
        // Llama al método del repositorio sin el Sort
        List<Empleado> empleadosInactivos = empleadoRepository.findByActivoFalse();
        return empleadosInactivos.stream()
                .map(empleadoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 120000) // Cada 2 minutos
    @Transactional
    @Override
    public void desbloquearEmpleadosBloqueadosAutomaticamente() {
        System.out.println("Ejecutando tarea programada: Comprobar y desbloquear empleados cuyo tiempo de bloqueo ha expirado (usando synchronized).");
        LocalDateTime ahora = LocalDateTime.now();

        List<UsuarioEmpleado> usuariosParaDesbloquear =
                usuarioEmpleadoRepository.findByActivoFalseAndFechaDesbloqueoIsNotNullAndFechaDesbloqueoLessThanEqual(ahora);

        if (!usuariosParaDesbloquear.isEmpty()) {
            List<String> nombresDesbloqueadosEstaVez = new ArrayList<>();
            for (UsuarioEmpleado usuario : usuariosParaDesbloquear) {
                logger.debug("Desbloqueando usuario: {} cuya fecha de desbloqueo ({}) ha pasado.",
                        usuario.getUsuario(), usuario.getFechaDesbloqueo());
                usuario.setIntentosSesionFallidos(0);
                usuario.setMotivoBloqueo(null);
                usuario.setFechaDesbloqueo(null);
                usuarioEmpleadoRepository.save(usuario);

                Empleado empleadoAsociado = empleadoRepository.findByUsuario(usuario);
                if (empleadoAsociado != null) {
                    // Asumiendo que Empleado tiene getNombre() y getApellido1()
                    String nombreCompleto = (empleadoAsociado.getNombre() != null ? empleadoAsociado.getNombre() : "") +
                            (empleadoAsociado.getApellido() != null ? " " + empleadoAsociado.getApellido() : "");
                    nombreCompleto = nombreCompleto.trim();
                    if (nombreCompleto.isEmpty()) {
                        nombreCompleto = "ID Empleado: " + empleadoAsociado.getId(); // Fallback si nombre y apellido son nulos/vacíos
                    }
                    nombresDesbloqueadosEstaVez.add(nombreCompleto);
                    logger.info("Empleado {} (Usuario: {}) desbloqueado automáticamente por expiración de fecha.",
                            nombreCompleto, usuario.getUsuario());

                } else {
                    String identificadorUsuario = "Usuario: " + usuario.getUsuario() + " (ID: " + usuario.getId() + ")";
                    nombresDesbloqueadosEstaVez.add(identificadorUsuario);
                    logger.warn("Usuario {} desbloqueado por expiración de fecha, pero no se encontró Empleado asociado.",
                            identificadorUsuario);
                }
            }

            synchronized (this.empleadosDesbloqueadosRecientemente) {
                this.empleadosDesbloqueadosRecientemente.clear();
                if (!nombresDesbloqueadosEstaVez.isEmpty()) {
                    this.empleadosDesbloqueadosRecientemente.addAll(nombresDesbloqueadosEstaVez);
                    logger.info("{} empleados/usuarios han sido desbloqueados por expiración y añadidos a la lista de notificación.",

                            nombresDesbloqueadosEstaVez.size());
                }
            }
        } else {
            logger.info("No hay empleados con bloqueo temporal expirado para desbloquear en esta ejecución.");
            if (!this.empleadosDesbloqueadosRecientemente.isEmpty()) {
                synchronized (this.empleadosDesbloqueadosRecientemente) {
                    this.empleadosDesbloqueadosRecientemente.clear();
                }
            }
        }
    }

    @Override
    public List<String> getNombresEmpleadosDesbloqueadosRecientemente() {
        synchronized (this.empleadosDesbloqueadosRecientemente) {
            if (this.empleadosDesbloqueadosRecientemente.isEmpty()) {
                return Collections.emptyList();
            }
            return new ArrayList<>(this.empleadosDesbloqueadosRecientemente);
        }
    }

    @Override
    public void clearNombresEmpleadosDesbloqueadosRecientemente() {
        synchronized (this.empleadosDesbloqueadosRecientemente) {
            this.empleadosDesbloqueadosRecientemente.clear();
            logger.debug("Lista de empleados desbloqueados recientemente ha sido limpiada.");
        }
    }


    public List<Especialidad> devuelveListaEspecialidades() {
        return especialidadRepository.findAll();
    }
    public List<Departamento> devolverDepartamentos(){
        return departamentoRepository.findAll();
    }
    public List<EntidadBancaria> devolverEntidadesBancarias(){
        return entidadBancariaRepository.findAll();
    }
    public List<TipoTarjetaCredito> devolverTipoTarjetasCredito(){
        return tipoTarjetaRepository.findAll();
    }


    public UsuarioEmpleado devuelveUsuarioEmpleado(String id){
        return usuarioEmpleadoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UsuarioNoEncontradoException("Empleado no encontrado"));
    }
}
