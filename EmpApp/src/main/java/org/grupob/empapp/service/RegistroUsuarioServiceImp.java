package org.grupob.empapp.service;

import org.grupob.empapp.converter.RegistroUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.empapp.entity.Empleado;
import org.grupob.empapp.entity.UsuarioEmpleado;
import org.grupob.empapp.exception.UsuarioYaExisteException;
import org.grupob.empapp.repository.UsuarioEmpleadoRepository;
import org.grupob.empapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroUsuarioServiceImp implements RegistroUsuarioService{

    UsuarioEmpleadoRepository usuarioEmpleadoRepository;
    RegistroUsuarioEmpleadoConverter registroUsuarioEmpleadoConverter;

    public RegistroUsuarioServiceImp(UsuarioEmpleadoRepository usuarioEmpleadoRepository, RegistroUsuarioEmpleadoConverter registroUsuarioEmpleadoConverter) {
        this.usuarioEmpleadoRepository = usuarioEmpleadoRepository;
        this.registroUsuarioEmpleadoConverter = registroUsuarioEmpleadoConverter;
    }

    public void guardarUsuario(RegistroUsuarioEmpleadoDTO usuario) {
        UsuarioEmpleado usuarioEmpleado = new UsuarioEmpleado();
    }

    public void usuarioExiste(RegistroUsuarioEmpleadoDTO registroUsuarioEmpleadoDTO){
        List<UsuarioEmpleado> usuarioEmpleado = usuarioEmpleadoRepository.findAll();
        usuarioEmpleado
                .forEach(u -> {
                    if (u.getUsuario().equals(registroUsuarioEmpleadoDTO.getUsuario())) {
                        throw new UsuarioYaExisteException("Este usuario ya existe");
                    }
                });
    }
//    public void guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO){
//        Empleado empleado = empleadoConverter.convertirAEntidad(altaEmpleadoDTO);
//        empleado.setGenero(generoRepository.findById(altaEmpleadoDTO.getIdGeneroSeleccionado()).orElseThrow());
//        System.err.println(empleado);
//        empleadoRepository.save(empleado);
//    }
    //public Empleado
}
