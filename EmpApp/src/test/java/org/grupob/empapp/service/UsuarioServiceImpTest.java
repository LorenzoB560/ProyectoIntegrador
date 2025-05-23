package org.grupob.empapp.service;

import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.converter.LoginUsuarioEmpleadoConverter;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.grupob.comun.exception.ClaveIncorrectaException;
import org.grupob.comun.exception.CuentaBloqueadaException;
import org.grupob.comun.exception.UsuarioNoEncontradoException;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.grupob.comun.repository.maestras.MotivoBloqueoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioEmpleadoServiceImpTest {

    // Mocks de las dependencias del servicio (repositorios y conversor)
    @Mock
    private UsuarioEmpleadoRepository usuarioEmpRepo;
    @Mock
    private MotivoBloqueoRepository motivoBloqueoRepo;
    @Mock
    private LoginUsuarioEmpleadoConverter loginUsuarioEmpConvert;

    // Instancia del servicio que vamos a probar, con los mocks inyectados
    @InjectMocks
    private UsuarioEmpleadoServiceImp service;

    // Codificador real para pruebas de contraseñas
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    // Inicializa los mocks antes de cada test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba: Cuando buscamos un usuario existente, debe devolver su DTO
    @Test
    void devuelveUsuarioEmpPorUsuario_usuarioExiste_devuelveDTO() {
        // Configurar datos de prueba
        UsuarioEmpleado usuario = new UsuarioEmpleado("test@correo.com", "clave");
        LoginUsuarioEmpleadoDTO dto = new LoginUsuarioEmpleadoDTO("test@correo.com", "clave");

        // Simular comportamiento del repositorio y conversor
        when(usuarioEmpRepo.findByUsuario("test@correo.com")).thenReturn(Optional.of(usuario));
        when(loginUsuarioEmpConvert.convertirADTO(usuario)).thenReturn(dto);

        // Ejecutar método bajo prueba
        LoginUsuarioEmpleadoDTO result = service.devuelveUsuarioEmpPorUsuario("test@correo.com");

        // Verificar resultado esperado
        assertNotNull(result);
        assertEquals("test@correo.com", result.getUsuario());
    }

    // Prueba: Al buscar un usuario inexistente debe lanzar excepción
    @Test
    void devuelveUsuarioEmpPorUsuario_usuarioNoExiste_lanzaExcepcion() {
        // Simular que el repositorio no encuentra el usuario
        when(usuarioEmpRepo.findByUsuario("no@existe.com")).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción esperada
        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.devuelveUsuarioEmpPorUsuario("no@existe.com"));
    }

    // Prueba: Validar email de usuario bloqueado temporalmente
    @Test
    void validarEmail_usuarioBloqueado_lanzaExcepcion() {
        // Crear usuario bloqueado con fecha futura
        UsuarioEmpleado usuario = new UsuarioEmpleado();
        usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(10));

        // Configurar mock para devolver usuario bloqueado
        when(usuarioEmpRepo.findByUsuario("bloqueado@correo.com")).thenReturn(Optional.of(usuario));

        // Verificar que se lanza excepción de cuenta bloqueada
        assertThrows(CuentaBloqueadaException.class,
                () -> service.validarEmail("bloqueado@correo.com"));
    }

    // Prueba: Credenciales correctas actualizan estadísticas de acceso
    @Test
    void validarCredenciales_credencialesCorrectas_devuelveTrue() {
        // Crear usuario con contraseña cifrada
        UsuarioEmpleado usuario = new UsuarioEmpleado("test@correo.com", encoder.encode("clave123"));
        LoginUsuarioEmpleadoDTO dto = new LoginUsuarioEmpleadoDTO("test@correo.com", "clave123");

        // Configurar mock para devolver usuario
        when(usuarioEmpRepo.findByUsuario("test@correo.com")).thenReturn(Optional.of(usuario));

        // Ejecutar validación y verificar resultado
        assertTrue(service.validarCredenciales(dto));

        // Confirmar que se actualizaron las estadísticas (save() fue llamado)
        verify(usuarioEmpRepo).save(usuario);
    }

    // Prueba: Contraseña incorrecta incrementa intentos fallidos
    @Test
    void validarCredenciales_claveIncorrecta_incrementaIntentos() {
        // Configurar usuario con contraseña válida
        UsuarioEmpleado usuario = new UsuarioEmpleado("test@correo.com", encoder.encode("clave123"));
        LoginUsuarioEmpleadoDTO dto = new LoginUsuarioEmpleadoDTO("test@correo.com", "incorrecta");

        // Simular búsqueda de usuario
        when(usuarioEmpRepo.findByUsuario("test@correo.com")).thenReturn(Optional.of(usuario));

        // Verificar que se lanza excepción por clave incorrecta
        assertThrows(ClaveIncorrectaException.class, () -> service.validarCredenciales(dto));

        // Confirmar que los intentos se incrementaron a 1
        assertEquals(1, usuario.getIntentosSesionFallidos());
        // Verificar que se guardaron los cambios
        verify(usuarioEmpRepo).save(usuario);
    }

    // Prueba: Tres intentos fallidos bloquean la cuenta
    @Test
    void validarCredenciales_maximosIntentos_bloqueaCuenta() {
        // Crear usuario con 2 intentos fallidos previos
        UsuarioEmpleado usuario = new UsuarioEmpleado("test@correo.com", encoder.encode("clave123"));
        usuario.setIntentosSesionFallidos(2);

        // Configurar motivo de bloqueo con 10 minutos de duración
        MotivoBloqueo motivo = new MotivoBloqueo();
        motivo.setMinutos(10);

        // Configurar mocks para repositorios
        when(usuarioEmpRepo.findByUsuario("test@correo.com")).thenReturn(Optional.of(usuario));
        when(motivoBloqueoRepo.findById(1L)).thenReturn(Optional.of(motivo));

        // Ejecutar con tercera contraseña incorrecta
        assertThrows(ClaveIncorrectaException.class,
                () -> service.validarCredenciales(new LoginUsuarioEmpleadoDTO("test@correo.com", "incorrecta")));

        // Verificaciones post-ejecución
        assertNotNull(usuario.getFechaDesbloqueo());  // Debe tener fecha de desbloqueo
        assertEquals(3, usuario.getIntentosSesionFallidos());  // Intentos actualizados a 3
        verify(usuarioEmpRepo, times(1)).save(usuario);  // Se guarda solo una vez
    }

    // Prueba: Actualizar contraseña de usuario existente
    @Test
    void actualizarClave_usuarioExiste_actualizaClave() {
        // Crear usuario con contraseña antigua
        UsuarioEmpleado usuario = new UsuarioEmpleado("test@correo.com", "oldHash");
        when(usuarioEmpRepo.findByUsuario("test@correo.com")).thenReturn(Optional.of(usuario));

        // Ejecutar actualización de contraseña
        service.actualizarClave("test@correo.com", "nuevaClave123");

        // Verificar que la contraseña cambió
        assertNotEquals("oldHash", usuario.getClave());
        // Confirmar que se guardaron los cambios
        verify(usuarioEmpRepo).save(usuario);
    }
}


