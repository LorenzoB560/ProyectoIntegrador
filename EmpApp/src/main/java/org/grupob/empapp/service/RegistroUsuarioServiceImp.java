package org.grupob.empapp.service;

import org.grupob.empapp.converter.RegistroUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.exception.UsuarioYaExisteException;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroUsuarioServiceImp implements RegistroUsuarioService{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        usuarioEmpleado.setIntentosSesionFallidos(0);
        usuarioEmpleado.setNumeroAccesos(0);
        usuarioEmpleado.setActivo(true);
        usuarioEmpleado.setFechaCreacion(LocalDateTime.now());
        usuarioEmpleadoRepository.save(usuarioEmpleado);
        logger.info("El usuario {} se creado correctamente", usuario.getUsuario());
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
}
