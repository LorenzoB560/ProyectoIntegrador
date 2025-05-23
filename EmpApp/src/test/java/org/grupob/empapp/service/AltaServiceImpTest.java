package org.grupob.empapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.entity.*;
import org.grupob.comun.entity.maestras.*;
import org.grupob.comun.repository.*;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.grupob.empapp.converter.CuentaBancariaConverter;
import org.grupob.empapp.converter.EmpleadoConverterEmp;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.CuentaBancariaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Year;
import java.util.*;

class AltaEmpleadoServiceImpTest {

    // Se crean mocks de todos los repositorios y convertidores utilizados en el servicio
    @Mock private GeneroRepository generoRepository;
    @Mock private EmpleadoRepository empleadoRepository;
    @Mock private PaisRepository paisRepository;
    @Mock private TipoViaRepository tipoViaRepository;
    @Mock private DepartamentoRepository departamentoRepository;
    @Mock private TipoDocumentoRepository tipoDocumentoRepository;
    @Mock private EspecialidadRepository especialidadRepository;
    @Mock private EntidadBancariaRepository entidadBancariaRepository;
    @Mock private TipoTarjetaRepository tipoTarjetaRepository;
    @Mock private EmpleadoConverterEmp empleadoConverter;
    @Mock private CuentaBancariaConverter cuentaBancariaConverter;

    // El servicio bajo prueba, con los mocks inyectados
    @InjectMocks
    private AltaEmpleadoServiceImp service;

    // Antes de cada test, inicializamos los mocks para garantizar un entorno limpio y controlado
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para comprobar que se devuelven todos los géneros correctamente
    @Test
    void devolverGeneros_devuelveLista() {
        // Simulamos que el repositorio devuelve dos géneros
        List<Genero> generos = List.of(new Genero(), new Genero());
        when(generoRepository.findAll()).thenReturn(generos);

        // Ejecutamos el método y comprobamos que la lista tiene dos elementos
        List<Genero> result = service.devolverGeneros();
        assertEquals(2, result.size());
        verify(generoRepository).findAll();
    }

    // Test para comprobar que se devuelven todos los países correctamente
    @Test
    void devolverPaises_devuelveLista() {
        // Simulamos dos países en el repositorio
        List<Pais> paises = List.of(new Pais(), new Pais());
        when(paisRepository.findAll()).thenReturn(paises);

        // Ejecutamos y verificamos el tamaño de la lista
        List<Pais> result = service.devolverPaises();
        assertEquals(2, result.size());
        verify(paisRepository).findAll();
    }

    // Test para comprobar que se devuelven todos los tipos de vía
    @Test
    void devolverTipoVias_devuelveLista() {
        // Simulamos un tipo de vía en el repositorio
        List<TipoVia> vias = List.of(new TipoVia());
        when(tipoViaRepository.findAll()).thenReturn(vias);

        // Ejecutamos y verificamos el tamaño de la lista
        List<TipoVia> result = service.devolverTipoVias();
        assertEquals(1, result.size());
        verify(tipoViaRepository).findAll();
    }

    // Test para comprobar que se devuelven todos los tipos de documento
    @Test
    void devolverTipoDocumentos_devuelveLista() {
        // Simulamos un tipo de documento en el repositorio
        List<TipoDocumento> docs = List.of(new TipoDocumento());
        when(tipoDocumentoRepository.findAll()).thenReturn(docs);

        // Ejecutamos y verificamos el tamaño de la lista
        List<TipoDocumento> result = service.devolverTipoDocumentos();
        assertEquals(1, result.size());
        verify(tipoDocumentoRepository).findAll();
    }

    // Test para comprobar que se devuelven todos los departamentos
    @Test
    void devolverDepartamentos_devuelveLista() {
        // Simulamos un departamento en el repositorio
        List<Departamento> deps = List.of(new Departamento());
        when(departamentoRepository.findAll()).thenReturn(deps);

        // Ejecutamos y verificamos el tamaño de la lista
        List<Departamento> result = service.devolverDepartamentos();
        assertEquals(1, result.size());
        verify(departamentoRepository).findAll();
    }

    // Test para comprobar que se devuelven todas las especialidades
    @Test
    void devolverEspecialidades_devuelveLista() {
        // Simulamos una especialidad en el repositorio
        List<Especialidad> especialidades = List.of(new Especialidad());
        when(especialidadRepository.findAll()).thenReturn(especialidades);

        // Ejecutamos y verificamos el tamaño de la lista
        List<Especialidad> result = service.devolverEspecialidades();
        assertEquals(1, result.size());
        verify(especialidadRepository).findAll();
    }

    // Test para comprobar que se devuelven todas las entidades bancarias
    @Test
    void devolverEntidadesBancarias_devuelveLista() {
        // Simulamos una entidad bancaria en el repositorio
        List<EntidadBancaria> entidades = List.of(new EntidadBancaria());
        when(entidadBancariaRepository.findAll()).thenReturn(entidades);

        // Ejecutamos y verificamos el tamaño de la lista
        List<EntidadBancaria> result = service.devolverEntidadesBancarias();
        assertEquals(1, result.size());
        verify(entidadBancariaRepository).findAll();
    }

    // Test para comprobar que se devuelven todos los tipos de tarjeta de crédito
    @Test
    void devolverTipoTarjetasCredito_devuelveLista() {
        // Simulamos un tipo de tarjeta de crédito en el repositorio
        List<TipoTarjetaCredito> tarjetas = List.of(new TipoTarjetaCredito());
        when(tipoTarjetaRepository.findAll()).thenReturn(tarjetas);

        // Ejecutamos y verificamos el tamaño de la lista
        List<TipoTarjetaCredito> result = service.devolverTipoTarjetasCredito();
        assertEquals(1, result.size());
        verify(tipoTarjetaRepository).findAll();
    }

    // Test para comprobar que se devuelven los 12 meses correctamente formateados
    @Test
    void devolverMeses_devuelveLista12() {
        // Ejecutamos el método y comprobamos que la lista tiene 12 elementos, del "01" al "12"
        List<String> meses = service.devolverMeses();
        assertEquals(12, meses.size());
        assertEquals("01", meses.get(0));
        assertEquals("12", meses.get(11));
    }

    // Test para comprobar que se devuelven 21 años a partir del actual
    @Test
    void devolverAnios_devuelveLista21() {
        // Calculamos el año actual y ejecutamos el método
        int anioActual = Year.now().getValue();
        List<String> anios = service.devolverAnios();

        // Verificamos que hay 21 años y que el primero y último son correctos
        assertEquals(21, anios.size());
        assertEquals(String.valueOf(anioActual), anios.get(0));
        assertEquals(String.valueOf(anioActual + 20), anios.get(20));
    }

    // Test para comprobar si existe usuario cuando la sesión no es nula
    @Test
    void usuarioExiste_sesionNoNula_devuelveTrue() {
        // Creamos un DTO de sesión simulado
        LoginUsuarioEmpleadoDTO sesion = new LoginUsuarioEmpleadoDTO();
        // Debe devolver true porque la sesión existe
        assertTrue(service.usuarioExiste(sesion));
    }

    // Test para comprobar si existe usuario cuando la sesión es nula
    @Test
    void usuarioExiste_sesionNula_devuelveFalse() {
        // Debe devolver false porque la sesión es nula
        assertFalse(service.usuarioExiste(null));
    }

    // Test para actualizar datos cuando los datos nuevos son nulos (se crea una instancia nueva)
    @Test
    void actualizarDatos_datosNuevosNulos_creaInstanciaNueva() {
        AltaEmpleadoDTO datosAnteriores = new AltaEmpleadoDTO();
        datosAnteriores.setNombre("Juan");

        // Ejecutamos el método con datosNuevos nulo
        service.actualizarDatos(null, datosAnteriores);

        // No se puede verificar el objeto creado porque es local, pero el test asegura que no lanza excepción
    }

    // Test para actualizar datos cuando los datos anteriores son nulos (no hace nada)
    @Test
    void actualizarDatos_datosAnterioresNulos_noHaceNada() {
        AltaEmpleadoDTO datosNuevos = new AltaEmpleadoDTO();

        // Ejecutamos el método con datosAnteriores nulo
        service.actualizarDatos(datosNuevos, null);

        // El test asegura que no lanza excepción y no modifica nada relevante
    }

    // Test para actualizar datos mezclando campos de datos nuevos y anteriores
    @Test
    void actualizarDatos_camposMezclados_actualizaCorrectamente() {
        // Creamos datos nuevos y anteriores con campos distintos
        AltaEmpleadoDTO datosNuevos = new AltaEmpleadoDTO();
        datosNuevos.setNombre("NuevoNombre");
        AltaEmpleadoDTO datosAnteriores = new AltaEmpleadoDTO();
        datosAnteriores.setNombre("AnteriorNombre");
        datosAnteriores.setApellido("ApellidoAnterior");

        // Ejecutamos el método de actualización
        service.actualizarDatos(datosNuevos, datosAnteriores);

        // Verificamos que los campos nulos en datosNuevos se rellenan con los valores de datosAnteriores
        assertEquals("NuevoNombre", datosNuevos.getNombre());
        assertEquals("ApellidoAnterior", datosNuevos.getApellido());
    }
}

