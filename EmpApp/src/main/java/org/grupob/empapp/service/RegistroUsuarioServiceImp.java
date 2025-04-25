package org.grupob.empapp.service;

import org.grupob.empapp.converter.RegistroUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.exception.UsuarioYaExisteException;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroUsuarioServiceImp implements RegistroUsuarioService{

    private final UsuarioEmpleadoRepository usuarioEmpleadoRepository;
    private final RegistroUsuarioEmpleadoConverter registroUsuarioEmpleadoConverter;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Para hashear la contraseña


    public RegistroUsuarioServiceImp(UsuarioEmpleadoRepository usuarioEmpleadoRepository, RegistroUsuarioEmpleadoConverter registroUsuarioEmpleadoConverter) {
        this.usuarioEmpleadoRepository = usuarioEmpleadoRepository;
        this.registroUsuarioEmpleadoConverter = registroUsuarioEmpleadoConverter;
    }

    public void guardarUsuario(RegistroUsuarioEmpleadoDTO usuario) {
        UsuarioEmpleado usuarioEmpleado = registroUsuarioEmpleadoConverter.convertirAEntidad(usuario);
        usuarioEmpleado.setClave(passwordEncoder.encode(usuario.getClave())); // Hashear la contraseña con BCrypt
        usuarioEmpleadoRepository.save(usuarioEmpleado);
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
