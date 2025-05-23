    package org.grupob.adminapp.controller;



    import jakarta.persistence.EntityNotFoundException;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.grupob.adminapp.dto.EmpleadoDTO;
    import org.grupob.adminapp.dto.ModificacionEmpleadoDTO;
    import org.grupob.adminapp.service.EmpleadoServiceImp;
    import org.grupob.adminapp.service.EtiquetaServiceImp;
    import org.grupob.adminapp.service.UsuarioEmpleadoServiceImp;
    import org.grupob.comun.dto.EmpleadoSearchDTO;
    import org.grupob.comun.entity.Empleado;
    import org.grupob.comun.entity.UsuarioEmpleado;
    import org.grupob.comun.exception.DepartamentoNoEncontradoException;
    import org.grupob.comun.exception.EmpleadoNoEncontradoException;
    import org.grupob.comun.repository.EmpleadoRepository;


    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.data.domain.Page;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;

    @RestController
    @RequestMapping("empleados")
    @RequiredArgsConstructor
    public class EmpleadoRestController {

        private static final Logger logger = LoggerFactory.getLogger(EmpleadoRestController.class);

        private final EmpleadoRepository empleadoRepository;
        private final EmpleadoServiceImp empleadoService;
        private final EtiquetaServiceImp etiquetaService; // Inyectar EtiquetaService
        private final UsuarioEmpleadoServiceImp usuarioEmpleadoService;


        @GetMapping("/listado1")
        public List<EmpleadoDTO> listarEmpleados() {
            return empleadoService.devuelveTodosEmpleados();
        }

        @GetMapping("/listado")
        public Page<EmpleadoDTO> listarEmpleados(
                @Valid @ModelAttribute EmpleadoSearchDTO searchDTO,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "ename") String sortBy,
                @RequestParam(defaultValue = "asc") String sortDir) {

            return empleadoService.buscarEmpleadosPaginados(
                    searchDTO,
                    page, size, sortBy, sortDir);
        }

        @GetMapping("/todos-inactivos")
        public ResponseEntity<List<EmpleadoDTO>> listarTodosEmpleadosInactivos() {
            // Llama al servicio sin parámetros de ordenación
            List<EmpleadoDTO> empleadosInactivos = empleadoService.devuelveTodosEmpleadosInactivos();
            if (empleadosInactivos == null || empleadosInactivos.isEmpty()) { // Verificar null también
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(empleadosInactivos);
        }

        @GetMapping("/todos-activos")
        public ResponseEntity<List<EmpleadoDTO>> listarTodosEmpleadosActivos() {
            List<EmpleadoDTO> empleadosActivos = empleadoService.devuelveTodosEmpleadosActivos();
            if (empleadosActivos == null || empleadosActivos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(empleadosActivos);
        }

        @GetMapping("detalle/{id}")
        public EmpleadoDTO devuelveEmpleado(@PathVariable String id){
            return empleadoService.devuelveEmpleado(id);
        }

        @GetMapping("nombre/{nombre}")
        public Empleado devuelveEmpleadoPorNombre(@PathVariable String nombre){
            return empleadoService.devuleveEmpleadoPorNombre(nombre);
        }

        @PostMapping("alta")
        public Empleado guardarEmpleado(@RequestBody Empleado empleado){
            return empleadoService.guardarEmpleado(empleado);
        }

        @DeleteMapping("borrar/{id}")
        public void borrarEmpleado(@PathVariable String id){
            empleadoService.eliminaEmpleadoPorId(id);
        }

        @PutMapping("guardar-modificado/{id}")
        public ResponseEntity<?> guardarModificadoEmpleado(@PathVariable String id, @RequestBody ModificacionEmpleadoDTO empleadoDTO) {
            try {
                Empleado empleado = empleadoService.modificarEmpleado(id, empleadoDTO);

                return ResponseEntity.ok(empleado);
            } catch (EmpleadoNoEncontradoException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
            }
        }

        @PutMapping("/{id}/jefe/{jefeId}")
        public EmpleadoDTO asignarJefe(@PathVariable String id, @PathVariable String jefeId) {
            return empleadoService.asignarJefe(id, jefeId);
        }

        @DeleteMapping("/{id}/jefe")
        public EmpleadoDTO quitarJefe(@PathVariable String id) {
            return empleadoService.quitarJefe(id);
        }

        @GetMapping("/{id}/subordinados")
        public List<EmpleadoDTO> listarSubordinados(@PathVariable String id) {
            return empleadoService.listarSubordinados(id);
        }
        // --- NUEVO ENDPOINT PARA BUSCAR POR ETIQUETA ---
        @GetMapping("/por-etiqueta/{etiquetaId}")
        public ResponseEntity<List<EmpleadoDTO>> buscarEmpleadosPorEtiqueta(@PathVariable String etiquetaId) {
            try {
                // Validar UUID
                UUID.fromString(etiquetaId);
                List<EmpleadoDTO> empleados = etiquetaService.buscarEmpleadosPorEtiqueta(etiquetaId);
                return ResponseEntity.ok(empleados);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // ID de etiqueta inválido
            } catch (DepartamentoNoEncontradoException e) { // Si la etiqueta no existe
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>()); // Devuelve lista vacía en Not Found
            } catch (Exception e) {
                System.err.println("Error al buscar empleados por etiqueta: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @PostMapping("/{id}/desbloquear")
        public ResponseEntity<?> quitarBloqueo(@PathVariable String id) {
            try {
                // 1. Buscar al Empleado y su UsuarioEmpleado asociado

                UUID empleadoUuid = UUID.fromString(id); // Convertir String a UUID
                Empleado empleado = empleadoRepository.findById(empleadoUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

                UsuarioEmpleado usuario = empleado.getUsuario();
                if (usuario == null) {
                    // Considera qué hacer si un empleado no tiene usuario (¿error?)
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario asociado al empleado no encontrado.");
                }

                // 2. Comprobar si el usuario YA está desbloqueado
                // Asumimos que "desbloqueado" significa motivoBloqueo es null
                if (usuario.getMotivoBloqueo() == null ) {
                    // Devolver un 400 Bad Request indicando que la operación no es aplicable
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El empleado ya se encuentra desbloqueado.");
                }

                // 3. Si está bloqueado, proceder a desbloquear
                usuarioEmpleadoService.desbloquearEmpleado(id); // Llama al servicio


                // 4. Devolver éxito
                return ResponseEntity.ok().build();

            } catch (IllegalArgumentException e) {
                // Captura el error si el ID proporcionado no es un UUID válido
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido: " + id);
            } catch (EntityNotFoundException e) {
                // Captura si el empleado no se encuentra
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                // Captura cualquier otra excepción inesperada
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error interno al intentar desbloquear el empleado: " + e.getMessage());
            }
        }

        // --- MÉTODO MODIFICADO ---
        @PostMapping("/{id}/bloquear")
        public ResponseEntity<?> asignarBloqueo(@PathVariable String id, @RequestParam Long motivoId) {
            try {
                // 1. Buscar al Empleado y su UsuarioEmpleado asociado
                UUID empleadoUuid = UUID.fromString(id); // Convertir String a UUID
                Empleado empleado = empleadoRepository.findById(empleadoUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

                UsuarioEmpleado usuario = empleado.getUsuario();
                if (usuario == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario asociado al empleado no encontrado.");
                }

                // 2. Comprobar si el usuario YA está bloqueado o inactivo
                // Usamos !isActivo() como indicador principal.
                if (usuario.getMotivoBloqueo() != null ) {
                    // Devolver un 400 Bad Request indicando que la operación no es aplicable
                    // Puedes añadir el motivo actual si quieres:
                    String mensaje = "El empleado ya se encuentra bloqueado";
                    if (usuario.getMotivoBloqueo() != null) {
                        mensaje += " (Motivo: " + usuario.getMotivoBloqueo().getMotivo() + ")";
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
                }

                // 3. Si está activo, proceder a bloquear
                usuarioEmpleadoService.bloquearEmpleado(id, motivoId); // Llama al servicio


                // 4. Devolver éxito (el JS redirigirá)
                return ResponseEntity.ok().build();

            } catch (IllegalArgumentException e) {
                // Captura el error si el ID proporcionado no es un UUID válido
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido: " + id);
            } catch (EntityNotFoundException e) {
                // Captura si el empleado o el motivo (dentro del service) no se encuentran
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                // Captura cualquier otra excepción inesperada
                // Loggear error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error interno al intentar bloquear el empleado: " + e.getMessage());
            }
        }
        @PostMapping("/{id}/desactivar") // O @PostMapping("/{id}/desactivar")
        public ResponseEntity<?> desactivarEmpleado(@PathVariable String id) {
            try {
                // Lógica para encontrar y actualizar el empleado
                UUID empleadoUuid = UUID.fromString(id);
                Empleado empleado = empleadoRepository.findById(empleadoUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

                if (!empleado.isActivo()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El empleado ya se encuentra desactivado.");
                }

                empleado.setActivo(false);
                empleadoRepository.save(empleado);

                return ResponseEntity.ok().body("Empleado desactivado correctamente.");

            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido: " + id);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                // Loggear el error
                System.err.println("Error al desactivar empleado: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error interno al intentar desactivar el empleado: " + e.getMessage());
            }
        }

        @PostMapping("/{id}/activar")
        public ResponseEntity<?> activarEmpleado(@PathVariable String id) {
            try {
                UUID empleadoUuid = UUID.fromString(id);
                Empleado empleado = empleadoRepository.findById(empleadoUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

                if (empleado.isActivo()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El empleado ya se encuentra activo.");
                }

                empleado.setActivo(true);
                empleadoRepository.save(empleado);
                return ResponseEntity.ok().body("Empleado activado correctamente.");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido: " + id);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al activar empleado: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error interno al intentar activar el empleado: " + e.getMessage());
            }
        }
        @GetMapping("/desbloqueados-recientemente")
        public ResponseEntity<Map<String, List<String>>> getNotificacionDesbloqueos() {
            logger.info("Accediendo a endpoint /empleados/desbloqueados-recientemente");
            List<String> nombres = empleadoService.getNombresEmpleadosDesbloqueadosRecientemente();
            if (nombres != null && !nombres.isEmpty()) {
                empleadoService.clearNombresEmpleadosDesbloqueadosRecientemente();
                logger.info("Devolviendo {} nombres de empleados desbloqueados.", nombres.size());
                return ResponseEntity.ok(Map.of("nombres", nombres));
            }
            logger.info("No hay empleados recientemente desbloqueados para notificar.");
            return ResponseEntity.noContent().build();
        }


    }
