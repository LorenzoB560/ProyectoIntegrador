package org.grupob.adminapp.service;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.grupob.comun.repository.maestras.MotivoBloqueoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioEmpleadoServiceImpTest {

    @InjectMocks
    private UsuarioEmpleadoServiceImp service;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private UsuarioEmpleadoRepository usuarioEmpleadoRepository;

    @Mock
    private MotivoBloqueoRepository motivoBloqueoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para verificar que un empleado se bloquea correctamente cuando existe
    @Test
    void testBloquearEmpleado_exito() {
        // Creamos un UUID simulado para representar el ID del empleado
        UUID empleadoId = UUID.randomUUID();

        // Simulamos una entidad Empleado con un UsuarioEmpleado asociado
        UsuarioEmpleado usuario = new UsuarioEmpleado();
        Empleado empleado = new Empleado();
        empleado.setUsuario(usuario);

        // Creamos un motivo de bloqueo con una duración de 30 minutos
        MotivoBloqueo motivo = new MotivoBloqueo();
        motivo.setId(1L);
        motivo.setMinutos(30);

        // Simulamos la búsqueda exitosa del empleado y del motivo en los repositorios
        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));
        when(motivoBloqueoRepository.findById(1L)).thenReturn(Optional.of(motivo));

        // Ejecutamos el método que queremos probar
        service.bloquearEmpleado(empleadoId.toString(), 1L);

        // Verificamos que el usuario fue marcado como inactivo
        assertFalse(usuario.getActivo());

        // Verificamos que el motivo de bloqueo fue correctamente asignado
        assertEquals(motivo, usuario.getMotivoBloqueo());

        // Verificamos que la fecha de desbloqueo se asignó correctamente a 30 minutos en el futuro
        assertNotNull(usuario.getFechaDesbloqueo());
        assertTrue(usuario.getFechaDesbloqueo().isAfter(LocalDateTime.now().plusMinutes(29)));

        // Verificamos que se llamó a guardar el usuario bloqueado
        verify(usuarioEmpleadoRepository).save(usuario);
    }

    // Test para asegurarnos de que se lanza excepción si el empleado no existe
    @Test
    void testBloquearEmpleado_empleadoNoEncontrado() {
        String idInexistente = UUID.randomUUID().toString();
        when(empleadoRepository.findById(UUID.fromString(idInexistente))).thenReturn(Optional.empty());

        // Verificamos que se lanza la excepción esperada
        assertThrows(RuntimeException.class, () -> {
            service.bloquearEmpleado(idInexistente, 1L);
        });
    }

    // Test para asegurarnos de que se lanza excepción si el usuario no está asociado
    @Test
    void testBloquearEmpleado_sinUsuarioAsociado() {
        Empleado empleado = new Empleado();
        empleado.setUsuario(null);
        UUID empleadoId = UUID.randomUUID();

        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));

        assertThrows(IllegalStateException.class, () -> {
            service.bloquearEmpleado(empleadoId.toString(), 1L);
        });
    }

    // Test para verificar el desbloqueo correcto de un usuario
    @Test
    void testDesbloquearEmpleado_exito() {
        UUID empleadoId = UUID.randomUUID();

        // Usuario bloqueado actualmente
        UsuarioEmpleado usuario = new UsuarioEmpleado();
        usuario.setActivo(false);
        usuario.setMotivoBloqueo(new MotivoBloqueo());
        usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(10));

        Empleado empleado = new Empleado();
        empleado.setUsuario(usuario);

        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));

        service.desbloquearEmpleado(empleadoId.toString());

        // Verificamos que se limpió el motivo y fecha de desbloqueo
        assertNull(usuario.getMotivoBloqueo());
        assertNull(usuario.getFechaDesbloqueo());

        // Verificamos que se activó el usuario
        assertTrue(usuario.getActivo());

        verify(usuarioEmpleadoRepository).save(usuario);
    }
}
