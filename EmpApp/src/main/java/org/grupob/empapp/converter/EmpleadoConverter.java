package org.grupob.empapp.converter;

import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoConverter {
    ModelMapper modelMapper;


    public EmpleadoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Empleado convertirAEntidad(AltaEmpleadoDTO altaEmpleadoDTO){
        return modelMapper.map(altaEmpleadoDTO, Empleado.class);
    }

    public EmpleadoDTO convertToDto(Empleado empleado) {
//     Crear DTO base
        EmpleadoDTO empleadoDto = modelMapper.map(empleado, EmpleadoDTO.class);
//        EmpleadoDTO empleadoDto = new EmpleadoDTO();
//
//        // Mapear propiedades bÃ¡sicas
//        empleadoDto.setId(empleado.getId());
//        empleadoDto.setEmpno(empleado.getEmpno());
//        empleadoDto.setEname(empleado.getEname());
//        empleadoDto.setJob(empleado.getJob());
//        empleadoDto.setHiradate(empleado.getHiradate());
//        empleadoDto.setSal(empleado.getSal());
//        empleadoDto.setCom(empleado.getCom());
//        empleadoDto.setSalariobruto(empleado.getSalariobruto());
//
//        // Mapear departamento manualmente
//        if (empleado.getDepartamento() != null) {
//            DepartamentoDTO deptoDto = new DepartamentoDTO();
//            deptoDto.setId(empleado.getDepartamento().getId());
//            deptoDto.setDname(empleado.getDepartamento().getDname());
//            deptoDto.setDeptno(empleado.getDepartamento().getDeptno());
//            deptoDto.setLoc(empleado.getDepartamento().getLoc());
//            empleadoDto.setDepartamento(deptoDto);
//        }
//
//        // Mapear jefe si existe
//        if (empleado.getJefe() != null) {
//            EmpleadoBasicoDTO jefeDto = new EmpleadoBasicoDTO();
//            jefeDto.setId(empleado.getJefe().getId());
//            jefeDto.setEname(empleado.getJefe().getEname());
//            jefeDto.setJob(empleado.getJefe().getJob());
//            empleadoDto.setJefe(jefeDto);
//        }
//
//        // Mapear etiquetas manualmente
//        if (empleado.getEmpleadoEtiquetas() != null && !empleado.getEmpleadoEtiquetas().isEmpty()) {
//            Set<EtiquetaDTO> etiquetasDto = empleado.getEmpleadoEtiquetas().stream()
//                    .map(ee -> {
//                        if (ee.getEtiqueta() != null) {
//                            EtiquetaDTO etiquetaDto = new EtiquetaDTO();
//                            etiquetaDto.setId(ee.getEtiqueta().getId());
//                            etiquetaDto.setNombre(ee.getEtiqueta().getNombre());
//                            etiquetaDto.setColor(ee.getEtiqueta().getColor());
//                            etiquetaDto.setDescripcion(ee.getEtiqueta().getDescripcion());
//                            return etiquetaDto;
//                        }
//                        return null;
//                    })
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toSet());
//
//            empleadoDto.setEtiquetas(etiquetasDto);
//        } else {
//            empleadoDto.setEtiquetas(new HashSet<>());
//        }

        return empleadoDto;
    }
}