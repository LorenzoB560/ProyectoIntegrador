package org.grupob.empapp.converter;

import org.grupob.comun.entity.*; // Importa todas las entidades necesarias
import org.grupob.comun.entity.auxiliar.CuentaBancaria;
import org.grupob.comun.entity.auxiliar.DireccionPostal; // Importar si lo necesitas para convertir desde Empleado
import org.grupob.comun.entity.auxiliar.Periodo;
import org.grupob.comun.entity.auxiliar.TarjetaCredito;
import org.grupob.comun.entity.maestras.Genero;
import org.grupob.comun.entity.maestras.TipoTarjetaCredito;
import org.grupob.empapp.dto.*;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO; // Importar DTO
import org.grupob.empapp.dto.auxiliar.GeneroDTO;
import org.modelmapper.ModelMapper; // Aún necesario para convertirAEntidad
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmpleadoConverter {

    // Inyectamos ModelMapper principalmente para convertirAEntidad
    private final ModelMapper modelMapper;

    public EmpleadoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // No se necesita configuración de TypeMap para Empleado->EmpleadoDTO aquí
    }

    // Este método puede seguir usando ModelMapper
    public Empleado convertirAEntidad(AltaEmpleadoDTO altaEmpleadoDTO) {
        // Podrías necesitar mapeo manual aquí también si AltaEmpleadoDTO es complejo
        Empleado empleado = modelMapper.map(altaEmpleadoDTO, Empleado.class);

        // Mapeo manual para la dirección si usas DTO en AltaEmpleadoDTO
        if (altaEmpleadoDTO.getDireccion() != null) {
            DireccionPostal dp = modelMapper.map(altaEmpleadoDTO.getDireccion(), DireccionPostal.class);
            // O mapeo manual completo si es necesario
            // DireccionPostal dp = new DireccionPostal();
            // dp.setTipoVia(altaEmpleadoDTO.getDireccion().getTipoVia());
            // ... etc ...
            empleado.setDireccion(dp);
        }
        // Asegúrate de mapear Genero, Departamento desde los IDs en AltaEmpleadoDTO
        // Esto requeriría inyectar los repositorios aquí o hacerlo en el servicio
        // Por simplicidad, asumimos que esto se maneja en el servicio que llama a este método.

        return empleado;
    }

    /**
     * Convierte una entidad Empleado a EmpleadoDTO de forma 100% manual
     * para evitar cualquier problema con proxies de Hibernate y ModelMapper.
     * @param empleado La entidad Empleado (puede contener proxies).
     * @return El EmpleadoDTO mapeado.
     */
    public EmpleadoDTO convertToDto(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        EmpleadoDTO dto = new EmpleadoDTO();

        // --- Mapeo Manual de Campos Simples ---
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setFechaNacimiento(empleado.getFechaNacimiento());
        dto.setComentarios(empleado.getComentarios());
        dto.setActivo(empleado.isActivo());
        dto.setSalario(empleado.getSalario());
        dto.setComision(empleado.getComision());
        dto.setTieneFoto(empleado.getFoto() != null && empleado.getFoto().length > 0);

        // --- Mapeo Manual de Embeddables ---
        Periodo periodo = empleado.getPeriodo();
        if (periodo != null) {
            dto.setPeriodo(new PeriodoDTO(periodo.getFechaInicio(), periodo.getFechaFin()));
        } else {
            dto.setPeriodo(null);
        }

        CuentaBancaria cb = empleado.getCuentaCorriente();
        if (cb != null) {
            dto.setCuentaCorriente(new CuentaBancariaDTO(cb.getIBAN()));
        } else {
            dto.setCuentaCorriente(null);
        }

        TarjetaCredito tc = empleado.getTarjetaCredito();
        if (tc != null) {
            dto.setTarjetaCredito(new TarjetaCreditoDTO(tc.getNumero(), tc.getMesCaducidad(), tc.getAnioCaducidad(), tc.getCVC()));
        } else {
            dto.setTarjetaCredito(null);
        }

        // --- Mapeo Manual Controlado para Relaciones Lazy ---

        // Usuario (para el correo)
        UsuarioEmpleado usuario = empleado.getUsuario();
        // Asumimos que el usuario siempre debe estar presente y cargado si es necesario
        // Si pudiera ser lazy y fallar: if (Hibernate.isInitialized(usuario) && usuario != null)
        if (usuario != null) {
            dto.setCorreo(usuario.getUsuario());
        } else {
            dto.setCorreo(null);
        }

        // Jefe
        Empleado jefe = empleado.getJefe();
        if (jefe != null && Hibernate.isInitialized(jefe)) {
            // Solo si está inicializado accedemos a sus propiedades
            dto.setIdJefe(jefe.getId()); // El ID se puede obtener a veces del proxy sin inicializar
            dto.setNombreJefe(jefe.getNombre());
        } else if (jefe != null) {
            // Si es un proxy no inicializado, al menos intentamos obtener el ID
            try {
                dto.setIdJefe(jefe.getId());
            } catch (Exception e) {
                dto.setIdJefe(null); // Falló al obtener ID del proxy
            }
            dto.setNombreJefe(null); // No podemos obtener el nombre
        }
        else {
            dto.setIdJefe(null);
            dto.setNombreJefe(null);
        }

        // Departamento
        Departamento depto = empleado.getDepartamento();
        if (depto != null && Hibernate.isInitialized(depto)) {
            dto.setDepartamento(new DepartamentoDTO(depto.getId(), depto.getCodigo(), depto.getNombre(), depto.getLocalidad()));
        } else {
            dto.setDepartamento(null);
        }

        // Genero
        Genero genero = empleado.getGenero();
        if (genero != null && Hibernate.isInitialized(genero)) {
            dto.setGenero(new GeneroDTO(genero.getId(), genero.getGenero()));
        } else {
            dto.setGenero(null);
        }

        // Entidad Bancaria
        EntidadBancaria eb = empleado.getEntidadBancaria();
        if (eb != null && Hibernate.isInitialized(eb)) {
            dto.setEntidadBancaria(new EntidadBancariaDTO(eb.getId(), eb.getCodigo(), eb.getNombre()));
        } else {
            dto.setEntidadBancaria(null);
        }

        // Tipo Tarjeta Credito
        TipoTarjetaCredito tt = empleado.getTipoTarjetaCredito();
        if (tt != null && Hibernate.isInitialized(tt)) {
            dto.setTipoTarjetaCredito(new TipoTarjetaCreditoDTO(tt.getId(), tt.getTipoTarjetaCredito()));
        } else {
            dto.setTipoTarjetaCredito(null);
        }

        // Colecciones (Especialidades y Etiquetas)
        Set<Especialidad> especialidades = empleado.getEspecialidades();
        Set<EspecialidadDTO> espDtos = new HashSet<>();
        if (especialidades != null && Hibernate.isInitialized(especialidades)) {
            for (Especialidad esp : especialidades) {
                if (esp != null) { // Comprobar nulidad del elemento
                    espDtos.add(new EspecialidadDTO(esp.getId(), esp.getNombre()));
                }
            }
        }
        dto.setEspecialidades(espDtos);

        Set<Etiqueta> etiquetas = empleado.getEtiquetas();
        Set<EtiquetaDTO> etDtos = new HashSet<>();
        if (etiquetas != null && Hibernate.isInitialized(etiquetas)) {
            for (Etiqueta et : etiquetas) {
                if (et != null) { // Comprobar nulidad del elemento
                    etDtos.add(new EtiquetaDTO(et.getId(), et.getNombre()));
                }
            }
        }
        dto.setEtiquetas(etDtos);

        return dto;
    }

    // Método para convertir páginas (llama al convertToDto manual)
    public Page<EmpleadoDTO> convertPageToDto(Page<Empleado> empleadoPage) {
        // El método map de Page usará nuestro convertToDto manual
        return empleadoPage.map(this::convertToDto);
    }
}