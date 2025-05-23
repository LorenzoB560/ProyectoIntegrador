package org.grupob.empapp.service;

import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.exception.UsuarioYaExisteException;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.grupob.empapp.converter.RegistroUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroUsuarioServiceImpTest {

    // Mocks de las dependencias externas del servicio
    @Mock
    private UsuarioEmpleadoRepository usuarioEmpleadoRepository;

    @Mock
    private RegistroUsuarioEmpleadoConverter converter;

    // Servicio bajo prueba con los mocks inyectados
    @InjectMocks
    private RegistroUsuarioServiceImp servicio;

    // Codificador real para verificar el hashing de contraseñas
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: Registrar usuario válido debe guardar con datos correctos
    @Test
    void guardarUsuario_usuarioValido_guardaCorrectamente() {
        // Configurar datos de prueba
        RegistroUsuarioEmpleadoDTO dto = new RegistroUsuarioEmpleadoDTO();
        dto.setUsuario("nuevo@usuario.com");
        dto.setClave("claveSegura123");

        UsuarioEmpleado usuarioMock = new UsuarioEmpleado();
        when(converter.convertirAEntidad(dto)).thenReturn(usuarioMock);

        // Ejecutar el método a testear
        servicio.guardarUsuario(dto);

        // Verificar que la contraseña se hasheó correctamente
        assertTrue(encoder.matches("claveSegura123", usuarioMock.getClave()));

        // Verificar valores por defecto
        assertEquals(0, usuarioMock.getIntentosSesionFallidos());
        assertEquals(0, usuarioMock.getNumeroAccesos());
        assertTrue(usuarioMock.getActivo());
        assertNotNull(usuarioMock.getFechaCreacion());

        // Confirmar que se llamó al repositorio para guardar
        verify(usuarioEmpleadoRepository).save(usuarioMock);
    }

    // Test: Validar usuario existente debe lanzar excepción
    @Test
    void usuarioExiste_usuarioYaExiste_lanzaExcepcion() {
        // Configurar escenario donde el usuario ya existe
        RegistroUsuarioEmpleadoDTO dto = new RegistroUsuarioEmpleadoDTO();
        dto.setUsuario("existente@usuario.com");

        UsuarioEmpleado usuarioExistente = new UsuarioEmpleado();
        usuarioExistente.setUsuario("existente@usuario.com");

        when(usuarioEmpleadoRepository.findAll()).thenReturn(List.of(usuarioExistente));

        // Verificar que se lanza la excepción esperada
        assertThrows(UsuarioYaExisteException.class,
                () -> servicio.usuarioExiste(dto));
    }

    // Test: Validar usuario nuevo no debe lanzar excepciones
    @Test
    void usuarioExiste_usuarioNuevo_noLanzaExcepcion() {
        // Configurar repositorio vacío
        RegistroUsuarioEmpleadoDTO dto = new RegistroUsuarioEmpleadoDTO();
        dto.setUsuario("nuevo@usuario.com");

        when(usuarioEmpleadoRepository.findAll()).thenReturn(Collections.emptyList());

        // Ejecutar validación y verificar que no hay excepciones
        assertDoesNotThrow(() -> servicio.usuarioExiste(dto));
    }

    // Test: Verificar que el hashing de contraseña funciona correctamente
    @Test
    void guardarUsuario_contraseñaSeHasheaCorrectamente() {
        // Configurar datos de prueba
        RegistroUsuarioEmpleadoDTO dto = new RegistroUsuarioEmpleadoDTO();
        dto.setClave("clave123");

        UsuarioEmpleado usuario = new UsuarioEmpleado();
        when(converter.convertirAEntidad(dto)).thenReturn(usuario);

        // Ejecutar método
        servicio.guardarUsuario(dto);

        // Verificar que la contraseña no está en texto plano
        assertNotEquals("clave123", usuario.getClave());
        // Verificar que el hash coincide con la contraseña original
        assertTrue(encoder.matches("clave123", usuario.getClave()));
    }

    // Test: Valores por defecto se establecen correctamente al guardar
    @Test
    void guardarUsuario_estableceValoresPorDefecto() {
        RegistroUsuarioEmpleadoDTO dto = new RegistroUsuarioEmpleadoDTO();
        dto.setClave("clave123");
        UsuarioEmpleado usuario = new UsuarioEmpleado();
        when(converter.convertirAEntidad(dto)).thenReturn(usuario);

        servicio.guardarUsuario(dto);

        // Verificar inicialización de campos
        assertEquals(0, usuario.getIntentosSesionFallidos());
        assertEquals(0, usuario.getNumeroAccesos());
        assertTrue(usuario.getActivo());
        // La fecha de creación debe ser reciente (margen de 1 segundo)
        assertTrue(LocalDateTime.now().minusSeconds(1).isBefore(usuario.getFechaCreacion()));
    }
}
