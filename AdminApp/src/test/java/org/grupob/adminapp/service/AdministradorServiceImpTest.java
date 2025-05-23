package org.grupob.adminapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.entity.Administrador;
import org.grupob.comun.exception.CredencialesInvalidasException;
import org.grupob.comun.exception.UsuarioNoEncontradoException;
import org.grupob.adminapp.converter.AdministradorConverter;
import org.grupob.comun.repository.AdministradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class AdministradorServiceImpTest {

    @Mock
    private AdministradorRepository adminRepo;

    @Mock
    private AdministradorConverter adminConverter;

    @InjectMocks
    private AdministradorServiceImp service;

    private final String TEST_USUARIO = "admin@empresa.com";
    private final String TEST_CLAVE = "claveSegura123";
    private final Administrador adminMock = new Administrador();
    private LoginAdministradorDTO dtoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración del Administrador mock
        adminMock.setUsuario(TEST_USUARIO);
        adminMock.setClave(TEST_CLAVE);
        adminMock.setNumeroAccesos(0);

        // Inicialización del DTO mock
        dtoMock = new LoginAdministradorDTO();
        dtoMock.setUsuario(TEST_USUARIO);
        dtoMock.setClave(TEST_CLAVE);

        // Configurar comportamiento del converter
        when(adminConverter.convertirADTO(adminMock)).thenReturn(dtoMock);
    }

    @Test
    void devuelveAdministradorPorUsuario_Existente_DevuelveEntidad() {
        when(adminRepo.findAdministradorByUsuario(TEST_USUARIO))
                .thenReturn(Optional.of(adminMock));

        Administrador resultado = service.devuelveAdministradorPorUsuario(TEST_USUARIO);

        assertNotNull(resultado);
        assertEquals(TEST_USUARIO, resultado.getUsuario());
        verify(adminRepo).findAdministradorByUsuario(TEST_USUARIO);
    }

    @Test
    void devuelveAdministradorPorUsuario_Inexistente_LanzaExcepcion() {
        when(adminRepo.findAdministradorByUsuario("noexiste@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.devuelveAdministradorPorUsuario("noexiste@test.com"));
    }

    @Test
    void comprobarCredenciales_CredencialesValidas_DevuelveDTO() {
        // 1. Configurar mocks
        LoginAdministradorDTO dtoEntrada = new LoginAdministradorDTO();
        dtoEntrada.setUsuario(TEST_USUARIO);
        dtoEntrada.setClave(TEST_CLAVE);

        // Simular repositorio
        when(adminRepo.findAdministradorByUsuario(TEST_USUARIO))
                .thenReturn(Optional.of(adminMock));

        // Simular guardado de admin (para el incremento de accesos)
        when(adminRepo.save(any(Administrador.class))).thenReturn(adminMock);

        // Configurar el converter para devolver un DTO válido (¡clave!)
        LoginAdministradorDTO dtoEsperado = new LoginAdministradorDTO();
        dtoEsperado.setUsuario(TEST_USUARIO);
        when(adminConverter.convertirADTO(any(Administrador.class)))
                .thenReturn(dtoEsperado); // Usar any() para capturar cualquier instancia

        // 2. Ejecutar método
        LoginAdministradorDTO resultado = service.comprobarCredenciales(dtoEntrada);

        // 3. Verificaciones
        assertNotNull(resultado, "El DTO resultado no debe ser null");
        assertEquals(TEST_USUARIO, resultado.getUsuario());

        // Verificar interacciones
        verify(adminRepo).findAdministradorByUsuario(TEST_USUARIO);
        verify(adminRepo).save(adminMock); // Asegurar que se actualizan los accesos
        verify(adminConverter).convertirADTO(any(Administrador.class));
    }

    @Test
    void comprobarCredenciales_UsuarioInvalido_LanzaExcepcion() {
        // Corregido: Usar setters en lugar de constructor no existente
        LoginAdministradorDTO dto = new LoginAdministradorDTO();
        dto.setUsuario("noexiste@test.com");
        dto.setClave(TEST_CLAVE);

        when(adminRepo.findAdministradorByUsuario("noexiste@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(CredencialesInvalidasException.class,
                () -> service.comprobarCredenciales(dto));
    }

    @Test
    void comprobarCredenciales_ClaveInvalida_LanzaExcepcion() {
        // Corregido: Usar setters en lugar de constructor no existente
        LoginAdministradorDTO dto = new LoginAdministradorDTO();
        dto.setUsuario(TEST_USUARIO);
        dto.setClave("claveErronea");

        when(adminRepo.findAdministradorByUsuario(TEST_USUARIO))
                .thenReturn(Optional.of(adminMock));

        assertThrows(CredencialesInvalidasException.class,
                () -> service.comprobarCredenciales(dto));
    }

    @Test
    void aumentarNumeroAccesos_IncrementaContador() {
        when(adminRepo.save(adminMock)).thenReturn(adminMock);

        Administrador resultado = service.aumentarNumeroAccesos(adminMock);

        assertEquals(1, resultado.getNumeroAccesos());
        verify(adminRepo).save(adminMock);
    }
}
