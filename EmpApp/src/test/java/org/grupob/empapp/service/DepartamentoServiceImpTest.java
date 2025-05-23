package org.grupob.empapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.grupob.comun.dto.DepartamentoDTO;
import org.grupob.comun.entity.Departamento;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.DepartamentoRepository;
import org.grupob.empapp.converter.DepartamentoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class DepartamentoServiceImpTest {

    // Mock del repositorio para simular acceso a datos
    @Mock
    private DepartamentoRepository departamentoRepository;

    // Mock del conversor para transformar entidades a DTOs
    @Mock
    private DepartamentoConverter departamentoConverter;

    // Servicio bajo prueba con las dependencias mockeadas
    @InjectMocks
    private DepartamentoServiceImp service;

    // Datos de prueba reutilizables
    private final UUID TEST_UUID = UUID.randomUUID();
    private final Departamento departamentoMock = new Departamento();

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
        departamentoMock.setId(TEST_UUID);
    }

    // Test: Obtener todos los departamentos devuelve lista no vacía
    @Test
    void devuelveTodosDepartamentos_ListaNoVacia() {
        // Configurar escenario con 2 departamentos mockeados
        when(departamentoRepository.findAll())
                .thenReturn(List.of(departamentoMock, new Departamento()));

        // Simular conversión a DTOs
        when(departamentoConverter.convertToDto(any(Departamento.class)))
                .thenReturn(new DepartamentoDTO());

        List<DepartamentoDTO> resultado = service.devuelveTodosDepartamentos();

        // Verificaciones
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        verify(departamentoRepository).findAll();
    }

    // Test: Buscar departamento existente por ID devuelve DTO
    @Test
    void devuelveDepartamento_Existente_DevuelveDTO() {
        // Configurar mock para devolver departamento
        when(departamentoRepository.getReferenceById(TEST_UUID))
                .thenReturn(departamentoMock);

        // Simular conversión
        DepartamentoDTO dtoMock = new DepartamentoDTO();
        when(departamentoConverter.convertToDto(departamentoMock))
                .thenReturn(dtoMock);

        DepartamentoDTO resultado = service.devuelveDepartamento(TEST_UUID.toString());

        assertNotNull(resultado);
        verify(departamentoRepository).getReferenceById(TEST_UUID);
    }

    // Test: Eliminar departamento existente ejecuta delete
    @Test
    void eliminaDepartamentoPorId_Existente_EjecutaDelete() {
        // Configurar existencia del departamento
        when(departamentoRepository.existsById(TEST_UUID))
                .thenReturn(true);

        // Ejecutar método
        assertThrows(DepartamentoNoEncontradoException.class,
                () -> service.eliminaDepartamentoPorId(TEST_UUID.toString()));

        // Verificar interacciones
        verify(departamentoRepository).existsById(TEST_UUID);
        verify(departamentoRepository).deleteById(TEST_UUID);
    }

    // Test: Eliminar departamento inexistente lanza excepción
    @Test
    void eliminaDepartamentoPorId_Inexistente_LanzaExcepcion() {
        // Configurar departamento inexistente
        when(departamentoRepository.existsById(TEST_UUID))
                .thenReturn(false);

        assertThrows(DepartamentoNoEncontradoException.class,
                () -> service.eliminaDepartamentoPorId(TEST_UUID.toString()));

        verify(departamentoRepository, never()).deleteById(any());
    }

    // Test: Modificar departamento existente actualiza datos
    @Test
    void modificarDepartamento_Existente_ActualizaCorrectamente() {
        // Configurar departamento existente
        when(departamentoRepository.existsById(TEST_UUID))
                .thenReturn(true);
        when(departamentoRepository.save(departamentoMock))
                .thenReturn(departamentoMock);

        Departamento resultado = service.modificarDepartamento(
                TEST_UUID.toString(),
                departamentoMock
        );

        assertNotNull(resultado);
        verify(departamentoRepository).save(departamentoMock);
    }

    // Test: Modificar departamento inexistente lanza error
    @Test
    void modificarDepartamento_Inexistente_LanzaExcepcion() {
        // Configurar departamento inexistente
        when(departamentoRepository.existsById(TEST_UUID))
                .thenReturn(false);

        assertThrows(DepartamentoNoEncontradoException.class,
                () -> service.modificarDepartamento(TEST_UUID.toString(), departamentoMock));

        verify(departamentoRepository, never()).save(any());
    }

    // Test: Buscar por nombre devuelve entidad existente
    @Test
    void devuleveDepartamentoPorNombre_Existente_DevuelveEntidad() {
        String nombreValido = "Ventas";
        when(departamentoRepository.findDepartamentoByNombre(nombreValido))
                .thenReturn(Optional.of(departamentoMock));

        Departamento resultado = service.devuleveDepartartamentoPorNombre(nombreValido);

        assertNotNull(resultado);
        verify(departamentoRepository).findDepartamentoByNombre(nombreValido);
    }
}

