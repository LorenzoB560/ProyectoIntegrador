package org.grupob.empapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EmpleadoSearchDTO;
import org.grupob.comun.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

class EmpleadoServiceImpTest {

    // Creamos mocks de las dependencias que usará el servicio
    @Mock
    private EmpleadoRepository empleadoRepo;

    @Mock
    private EmpleadoConverter empleadoConverter;

    // El servicio bajo prueba, con los mocks inyectados
    @InjectMocks
    private EmpleadoServiceImp service;

    // Variables de utilidad para los tests
    private final UUID TEST_UUID = UUID.randomUUID();
    private final Empleado empleadoMock = new Empleado();
    private final EmpleadoDTO dtoMock = new EmpleadoDTO();

    // Inicializamos los mocks antes de cada test para asegurar un entorno limpio
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empleadoMock.setId(TEST_UUID);
        when(empleadoConverter.convertToDto(empleadoMock)).thenReturn(dtoMock);
    }

    // Test para devolver todos los empleados como DTOs
    @Test
    void devuelveTodosEmpleados_devuelveListaDTO() {
        // Simulamos que el repositorio devuelve una lista con un empleado
        when(empleadoRepo.findAll()).thenReturn(List.of(empleadoMock));

        // Ejecutamos el método a testear
        List<EmpleadoDTO> result = service.devuelveTodosEmpleados();

        // Comprobamos que el resultado contiene exactamente un elemento
        assertEquals(1, result.size());
        // Verificamos que se haya llamado al conversor para transformar la entidad en DTO
        verify(empleadoConverter).convertToDto(empleadoMock);
    }

    // Test para devolver un empleado existente por id
    @Test
    void devuelveEmpleado_existente_devuelveDTO() {
        // Simulamos que el repositorio encuentra el empleado por su UUID
        when(empleadoRepo.findById(TEST_UUID)).thenReturn(Optional.of(empleadoMock));

        // Ejecutamos el método a testear
        EmpleadoDTO result = service.devuelveEmpleado(TEST_UUID.toString());

        // Comprobamos que el resultado no es nulo
        assertNotNull(result);
        // Verificamos que se haya buscado el empleado por id
        verify(empleadoRepo).findById(TEST_UUID);
    }

    // Test para eliminar un empleado inexistente
    @Test
    void eliminaEmpleadoPorId_inexistente_lanzaExcepcion() {
        // Simulamos que el empleado no existe en la base de datos
        when(empleadoRepo.existsById(TEST_UUID)).thenReturn(false);

        // Verificamos que se lanza la excepción al intentar eliminarlo
        assertThrows(DepartamentoNoEncontradoException.class,
                () -> service.eliminaEmpleadoPorId(TEST_UUID.toString()));
    }

    // Test para buscar empleados por departamento
    @Test
    void buscarEmpleadosPorDepartamento_conResultados() {
        // Simulamos que el repositorio devuelve una lista con un empleado
        when(empleadoRepo.findByDepartamentoNombreContaining("IT"))
                .thenReturn(List.of(empleadoMock));

        // Ejecutamos el método a testear
        List<EmpleadoDTO> result = service.buscarEmpleadosPorDepartamento("IT");

        // Comprobamos que la lista no está vacía
        assertFalse(result.isEmpty());
        // Verificamos que se haya convertido la entidad a DTO
        verify(empleadoConverter).convertToDto(empleadoMock);
    }

    // Test para búsqueda paginada con campo de ordenación inválido (debe usar el valor por defecto)
    @Test
    void buscarEmpleadosPaginados_ordenacionInvalida_usaDefault() {
        // Simulamos una página con un empleado
        Page<Empleado> page = new PageImpl<>(List.of(empleadoMock));
        when(empleadoRepo.buscarEmpleadosAvanzadoPaginado(
                any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        // Ejecutamos el método con un campo de ordenación inválido
        Page<EmpleadoDTO> result = service.buscarEmpleadosPaginados(
                "test", null, null, null, null,
                0, 10, "campo_invalido", "asc");

        // Comprobamos que el resultado no es nulo
        assertNotNull(result);
        // Verificamos que el repositorio recibió el campo de ordenación por defecto ("nombre")
        verify(empleadoRepo).buscarEmpleadosAvanzadoPaginado(
                eq("test"), eq(null), eq(null), eq(null), eq(null),
                argThat(pageable ->
                        pageable.getSort().getOrderFor("nombre") != null));
    }

    // Test para evitar que un empleado sea su propio jefe
    @Test
    void asignarJefe_autoreferencia_lanzaExcepcion() {
        // Creamos un empleado con su propio id
        Empleado empleado = new Empleado();
        empleado.setId(TEST_UUID);

        // Simulamos que el repositorio devuelve el empleado
        when(empleadoRepo.findById(TEST_UUID)).thenReturn(Optional.of(empleado));

        // Verificamos que se lanza una excepción si intenta asignarse como jefe
        assertThrows(RuntimeException.class,
                () -> service.asignarJefe(TEST_UUID.toString(), TEST_UUID.toString()));
    }

    // Test para quitar jefe a un empleado existente
    @Test
    void quitarJefe_empleadoExistente_actualizaRelacion() {
        // Creamos un empleado que tiene jefe
        Empleado empleadoConJefe = new Empleado();
        empleadoConJefe.setJefe(new Empleado());

        // Simulamos que el repositorio devuelve este empleado
        when(empleadoRepo.findById(TEST_UUID)).thenReturn(Optional.of(empleadoConJefe));

        // Ejecutamos el método para quitar el jefe
        service.quitarJefe(TEST_UUID.toString());

        // Comprobamos que el jefe ha sido eliminado (es null)
        assertNull(empleadoConJefe.getJefe());
        // Verificamos que se ha guardado el cambio
        verify(empleadoRepo).save(empleadoConJefe);
    }

    // Test para búsqueda avanzada sin filtrar por salario mínimo
    @Test
    void buscarEmpleadosAvanzado_salarioMinimoNull_noFiltra() {
        // Simulamos una página con un empleado
        Page<Empleado> page = new PageImpl<>(List.of(empleadoMock));
        when(empleadoRepo.buscarEmpleadosAvanzadoPaginado(
                any(), any(), any(), any(), eq(null), any()))
                .thenReturn(page);

        // Ejecutamos la búsqueda avanzada sin salario mínimo
        service.buscarEmpleadosAvanzado(null, null, null, null, null);

        // Verificamos que el repositorio fue llamado con salarioMinimo=null
        verify(empleadoRepo).buscarEmpleadosAvanzadoPaginado(
                eq(null), eq(null), eq(null), eq(null), eq(null), any());
    }

    // Test para listar subordinados de un jefe cuando no hay resultados
    @Test
    void listarSubordinados_sinResultados_devuelveListaVacia() {
        // Simulamos que no hay subordinados para el jefe dado
        when(empleadoRepo.findByJefe_Id(TEST_UUID)).thenReturn(Collections.emptyList());

        // Ejecutamos el método para listar subordinados
        List<EmpleadoDTO> result = service.listarSubordinados(TEST_UUID.toString());

        // Comprobamos que la lista está vacía
        assertTrue(result.isEmpty());
    }
}

