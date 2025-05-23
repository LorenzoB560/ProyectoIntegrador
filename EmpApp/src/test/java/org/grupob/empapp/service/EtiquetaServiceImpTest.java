package org.grupob.empapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Etiqueta;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.EtiquetaRepository;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.EtiquetaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.*;

class EtiquetaServiceImpTest {

    @Mock
    private EtiquetaRepository etiquetaRepo;

    @Mock
    private EmpleadoRepository empleadoRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmpleadoConverter empleadoConverter;

    @InjectMocks
    private EtiquetaServiceImp service;

    @PersistenceContext
    private EntityManager entityManager;

    private final UUID TEST_JEFE_ID = UUID.randomUUID();
    private final UUID TEST_EMPLEADO_ID = UUID.randomUUID();
    private final UUID TEST_ETIQUETA_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: Listar etiquetas de un jefe existente

    // Test: Crear nueva etiqueta cuando no existe
    @Test
    void buscarOCrearEtiqueta_etiquetaNueva_creaYGuarda() {
        // Configurar mocks
        String nombreEtiqueta = "Nueva Etiqueta";
        Empleado jefeMock = new Empleado();
        jefeMock.setId(TEST_JEFE_ID);

        Etiqueta etiquetaGuardada = new Etiqueta(nombreEtiqueta, jefeMock);
        // Simular que la etiqueta se guardará y el repositorio la devolverá
        when(etiquetaRepo.findByNombreIgnoreCaseAndCreador_Id(nombreEtiqueta, TEST_JEFE_ID))
                .thenReturn(Optional.empty());
        when(empleadoRepo.findById(TEST_JEFE_ID)).thenReturn(Optional.of(jefeMock));
        when(etiquetaRepo.save(any(Etiqueta.class))).thenReturn(etiquetaGuardada);

        // Ejecutar método
        Etiqueta resultado = service.buscarOCrearEtiqueta(nombreEtiqueta, TEST_JEFE_ID);

        // Verificaciones
        assertNotNull(resultado); // Ahora no será null
        assertEquals(nombreEtiqueta, resultado.getNombre());
        verify(etiquetaRepo).save(any(Etiqueta.class));
    }

    // Test: Eliminar etiqueta de empleado con relación existente

    // Test: Buscar empleados por etiqueta existente
    @Test
    void buscarEmpleadosPorEtiqueta_etiquetaValida_devuelveLista() {
        // Configurar datos
        Empleado creador = new Empleado();
        creador.setId(TEST_JEFE_ID);

        Etiqueta etiqueta = new Etiqueta("Organización", creador);
        etiqueta.getEmpleados().add(new Empleado());

        when(etiquetaRepo.findById(TEST_ETIQUETA_ID)).thenReturn(Optional.of(etiqueta));

        // Ejecutar método
        List<EmpleadoDTO> resultado = service.buscarEmpleadosPorEtiqueta(TEST_ETIQUETA_ID.toString());

        // Verificaciones
        assertFalse(resultado.isEmpty());
        verify(empleadoConverter).convertToDto(any(Empleado.class));
    }

    // Test: Intentar listar etiquetas de jefe inexistente
    @Test
    void listarEtiquetasPorJefe_jefeInexistente_lanzaExcepcion() {
        when(empleadoRepo.existsById(TEST_JEFE_ID)).thenReturn(false);

        assertThrows(DepartamentoNoEncontradoException.class,
                () -> service.listarEtiquetasPorJefe(TEST_JEFE_ID.toString()));
    }
}

