package org.grupob.empapp.converter;

import org.grupob.comun.entity.*; // Importa todas las entidades necesarias
import org.grupob.comun.entity.auxiliar.CuentaBancaria;
import org.grupob.comun.entity.auxiliar.Periodo;
import org.grupob.comun.entity.auxiliar.TarjetaCredito;
import org.grupob.comun.entity.maestras.Genero;
import org.grupob.comun.entity.maestras.TipoTarjetaCredito;
import org.grupob.empapp.dto.*;
import org.grupob.empapp.dto.auxiliar.GeneroDTO;
import org.modelmapper.ModelMapper; // ModelMapper aún puede ser útil para campos simples o el otro método
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmpleadoConverter {

    // ModelMapper todavía se puede inyectar y usar para convertirAEntidad
    // o para mapear objetos internos si se desea, pero no para el mapeo principal en convertToDto.
    private final ModelMapper modelMapper;

    public EmpleadoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Ya no necesitamos configurar el TypeMap para Empleado -> EmpleadoDTO aquí
        // porque lo haremos manualmente en convertToDto.
    }

    // Este método puede seguir usando ModelMapper si funciona bien para esta dirección
    public Empleado convertirAEntidad(AltaEmpleadoDTO altaEmpleadoDTO) {
        return modelMapper.map(altaEmpleadoDTO, Empleado.class);
    }

    /**
     * Convierte una entidad Empleado a EmpleadoDTO de forma manual
     * para evitar problemas con proxies de Hibernate y ModelMapper.
     * @param empleado La entidad Empleado (puede contener proxies).
     * @return El EmpleadoDTO mapeado.
     */
    public EmpleadoDTO convertToDto(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        EmpleadoDTO dto = new EmpleadoDTO();

        // --- Mapeo Manual de Campos Simples y Embeddables ---
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setFechaNacimiento(empleado.getFechaNacimiento());
        dto.setComentarios(empleado.getComentarios());
        dto.setActivo(empleado.isActivo());
        dto.setSalario(empleado.getSalario());
        dto.setComision(empleado.getComision());
        dto.setTieneFoto(empleado.getFoto() != null && empleado.getFoto().length > 0);

        // Embeddables (generalmente no son lazy)
        if (empleado.getPeriodo() != null) {
            // Puedes mapear manualmente o usar modelMapper para embeddables simples
            dto.setPeriodo(modelMapper.map(empleado.getPeriodo(), PeriodoDTO.class));
            // Alternativa manual:
            // Periodo p = empleado.getPeriodo();
            // dto.setPeriodo(new PeriodoDTO(p.getFechaInicio(), p.getFechaFin()));
        }
        if (empleado.getCuentaCorriente() != null) {
            dto.setCuentaCorriente(modelMapper.map(empleado.getCuentaCorriente(), CuentaBancariaDTO.class));
            // Alternativa manual:
            // dto.setCuentaCorriente(CuentaBancariaDTO.of(empleado.getCuentaCorriente().getIBAN()));
        }
        if (empleado.getTarjetaCredito() != null) {
            dto.setTarjetaCredito(modelMapper.map(empleado.getTarjetaCredito(), TarjetaCreditoDTO.class));
            // Alternativa manual:
            // TarjetaCredito tc = empleado.getTarjetaCredito();
            // dto.setTarjetaCredito(new TarjetaCreditoDTO(tc.getNumero(), tc.getMesCaducidad(), tc.getAnioCaducidad(), tc.getCVC()));
        }

        // --- Mapeo Manual Controlado para Relaciones Lazy ---

        // Usuario (para el correo)
        UsuarioEmpleado usuario = empleado.getUsuario();
        if (Hibernate.isInitialized(usuario) && usuario != null) {
            dto.setCorreo(usuario.getUsuario());
        } else {
            dto.setCorreo(null); // Opcional: intentar obtener ID si el proxy no está inicializado
        }

        // Jefe
        Empleado jefe = empleado.getJefe();
        if (Hibernate.isInitialized(jefe) && jefe != null) {
            dto.setIdJefe(jefe.getId());
            dto.setNombreJefe(jefe.getNombre()); // Acceder SÓLO después de inicializar/verificar
        } else {
            // Si es un proxy no inicializado, podríamos intentar obtener el ID si JPA lo permite
            // if (jefe != null) dto.setIdJefe(jefe.getId()); // Esto podría fallar también
            dto.setIdJefe(null);
            dto.setNombreJefe(null);
        }


        // Departamento
        Departamento depto = empleado.getDepartamento();
        if (Hibernate.isInitialized(depto) && depto != null) {
            // Mapear manualmente o usar modelMapper para el sub-objeto
            dto.setDepartamento(modelMapper.map(depto, DepartamentoDTO.class));
            // Alternativa manual:
            // dto.setDepartamento(new DepartamentoDTO(depto.getId(), depto.getCodigo(), depto.getNombre(), depto.getLocalidad()));
        } else {
            dto.setDepartamento(null);
        }

        // Genero
        Genero genero = empleado.getGenero();
        if (Hibernate.isInitialized(genero) && genero != null) {
            dto.setGenero(modelMapper.map(genero, GeneroDTO.class));
            // Alternativa manual:
            // dto.setGenero(new GeneroDTO(genero.getId(), genero.getGenero()));
        } else {
            dto.setGenero(null);
        }

        // Entidad Bancaria
        EntidadBancaria eb = empleado.getEntidadBancaria();
        if (Hibernate.isInitialized(eb) && eb != null) {
            dto.setEntidadBancaria(modelMapper.map(eb, EntidadBancariaDTO.class));
            // Alternativa manual:
            // dto.setEntidadBancaria(new EntidadBancariaDTO(eb.getId(), eb.getCodigo(), eb.getNombre()));
        } else {
            dto.setEntidadBancaria(null);
        }

        // Tipo Tarjeta Credito
        TipoTarjetaCredito tt = empleado.getTipoTarjetaCredito();
        if (Hibernate.isInitialized(tt) && tt != null) {
            dto.setTipoTarjetaCredito(modelMapper.map(tt, TipoTarjetaCreditoDTO.class));
            // Alternativa manual:
            // dto.setTipoTarjetaCredito(new TipoTarjetaCreditoDTO(tt.getId(), tt.getTipoTarjetaCredito()));
        } else {
            dto.setTipoTarjetaCredito(null);
        }


        // Colecciones (Especialidades y Etiquetas)
        Set<Especialidad> especialidades = empleado.getEspecialidades();
        Set<EspecialidadDTO> espDtos = new HashSet<>();
        if (Hibernate.isInitialized(especialidades) && especialidades != null) {
            // No es necesario inicializar cada elemento individualmente si la colección está inicializada
            espDtos = especialidades.stream()
                    .map(esp -> modelMapper.map(esp, EspecialidadDTO.class)) // Mapeo simple
                    .collect(Collectors.toSet());
        }
        dto.setEspecialidades(espDtos); // Asignar (podría ser vacío)

        Set<Etiqueta> etiquetas = empleado.getEtiquetas();
        Set<EtiquetaDTO> etDtos = new HashSet<>();
        if (Hibernate.isInitialized(etiquetas) && etiquetas != null) {
            etDtos = etiquetas.stream()
                    .map(et -> modelMapper.map(et, EtiquetaDTO.class)) // Mapeo simple
                    .collect(Collectors.toSet());
        }
        dto.setEtiquetas(etDtos); // Asignar (podría ser vacío)


        return dto;
    }

    // Método para convertir páginas (se mantiene igual, llama al convertToDto manual)
    public Page<EmpleadoDTO> convertPageToDto(Page<Empleado> empleadoPage) {
        return empleadoPage.map(this::convertToDto);
    }
}