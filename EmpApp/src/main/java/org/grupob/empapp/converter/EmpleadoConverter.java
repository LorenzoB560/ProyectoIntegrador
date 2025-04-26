package org.grupob.empapp.converter;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Etiqueta;
import org.grupob.empapp.dto.*;
import org.grupob.empapp.dto.auxiliar.GeneroDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap; // Importar TypeMap
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmpleadoConverter {

    private final ModelMapper modelMapper;

    public EmpleadoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configureModelMapper(); // Llamar a la configuración
    }

    private void configureModelMapper() {
        // 1. Crear un TypeMap explícito y vacío. Esto DESACTIVA el mapeo implícito inicial.
        TypeMap<Empleado, EmpleadoDTO> typeMap = modelMapper.typeMap(Empleado.class, EmpleadoDTO.class);

        // Si no existe, lo crea, si existe, lo devuelve para configuración adicional
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(Empleado.class, EmpleadoDTO.class);
        }


        // 2. Añadir mapeos explícitos necesarios (los que no son directos o requieren lógica)
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getUsuario() != null ? src.getUsuario().getUsuario() : null, EmpleadoDTO::setCorreo);
            mapper.map(src -> src.getJefe() != null ? src.getJefe().getId() : null, EmpleadoDTO::setIdJefe);
            mapper.map(src -> src.getFoto() != null && src.getFoto().length > 0, EmpleadoDTO::setTieneFoto);
            // No mapeamos nombreJefe aquí, se hará manualmente en convertToDto
            mapper.skip(EmpleadoDTO::setNombreJefe);
            // Ya NO necesitamos skip para departamento, genero, etiquetas, etc.
            // porque el TypeMap vacío inicial previene el mapeo implícito profundo.
            // ModelMapper ahora mapeará los campos simples (id, nombre, apellido, salario etc.)
            // implícitamente, pero no intentará mapear profundamente las relaciones complejas
            // que estamos saltando/manejando manualmente en convertToDto.
        });

        // 3. (Opcional) Podrías habilitar explícitamente el mapeo implícito para el resto
        //    después de tus configuraciones si fuera necesario, pero usualmente no lo es
        //    cuando manejas relaciones manualmente en el método de conversión.
        // typeMap.implicitMappings();
    }

    public Empleado convertirAEntidad(AltaEmpleadoDTO altaEmpleadoDTO) {
        return modelMapper.map(altaEmpleadoDTO, Empleado.class);
    }

    public EmpleadoDTO convertToDto(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        // Mapeo base usando la configuración del TypeMap
        EmpleadoDTO dto = modelMapper.map(empleado, EmpleadoDTO.class);

        // --- Mapeo Manual / Controlado para Relaciones Lazy/Complejas ---
        // (Esta lógica sigue siendo necesaria para evitar LazyInitializationException en runtime)

        // Jefe (nombre)
        if (Hibernate.isInitialized(empleado.getJefe()) && empleado.getJefe() != null) {
            dto.setNombreJefe(empleado.getJefe().getNombre());
        } else {
            dto.setNombreJefe(null);
        }

        // Departamento
        if (Hibernate.isInitialized(empleado.getDepartamento()) && empleado.getDepartamento() != null) {
            dto.setDepartamento(modelMapper.map(empleado.getDepartamento(), DepartamentoDTO.class));
        } else {
            dto.setDepartamento(null);
        }

        // Genero
        if (Hibernate.isInitialized(empleado.getGenero()) && empleado.getGenero() != null) {
            dto.setGenero(modelMapper.map(empleado.getGenero(), GeneroDTO.class));
        } else {
            dto.setGenero(null);
        }

        // Periodo (Embeddable)
        if (empleado.getPeriodo() != null) {
            dto.setPeriodo(modelMapper.map(empleado.getPeriodo(), PeriodoDTO.class));
        }

        // Cuenta Corriente (Embeddable)
        if (empleado.getCuentaCorriente() != null) {
            dto.setCuentaCorriente(modelMapper.map(empleado.getCuentaCorriente(), CuentaBancariaDTO.class));
        }

        // Tarjeta Credito (Embeddable)
        if (empleado.getTarjetaCredito() != null) {
            dto.setTarjetaCredito(modelMapper.map(empleado.getTarjetaCredito(), TarjetaCreditoDTO.class));
        }

        // Entidad Bancaria
        if (Hibernate.isInitialized(empleado.getEntidadBancaria()) && empleado.getEntidadBancaria() != null) {
            dto.setEntidadBancaria(modelMapper.map(empleado.getEntidadBancaria(), EntidadBancariaDTO.class));
        } else {
            dto.setEntidadBancaria(null);
        }

        // Tipo Tarjeta Credito
        if (Hibernate.isInitialized(empleado.getTipoTarjetaCredito()) && empleado.getTipoTarjetaCredito() != null) {
            dto.setTipoTarjetaCredito(modelMapper.map(empleado.getTipoTarjetaCredito(), TipoTarjetaCreditoDTO.class));
        } else {
            dto.setTipoTarjetaCredito(null);
        }

        // Colecciones (Especialidades y Etiquetas)
        Set<EspecialidadDTO> espDtos = new HashSet<>();
        if (Hibernate.isInitialized(empleado.getEspecialidades()) && empleado.getEspecialidades() != null) {
            espDtos = empleado.getEspecialidades().stream()
                    .map(esp -> modelMapper.map(esp, EspecialidadDTO.class))
                    .collect(Collectors.toSet());
        }
        dto.setEspecialidades(espDtos);

        Set<EtiquetaDTO> etDtos = new HashSet<>();
        if (Hibernate.isInitialized(empleado.getEtiquetas()) && empleado.getEtiquetas() != null) {
            etDtos = empleado.getEtiquetas().stream()
                    .map(et -> modelMapper.map(et, EtiquetaDTO.class))
                    .collect(Collectors.toSet());
        }
        dto.setEtiquetas(etDtos);

        return dto;
    }

    public Page<EmpleadoDTO> convertPageToDto(Page<Empleado> empleadoPage) {
        return empleadoPage.map(this::convertToDto);
    }
}