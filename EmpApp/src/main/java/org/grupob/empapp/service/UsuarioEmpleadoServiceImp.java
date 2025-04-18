package org.grupob.empapp.service;

import org.grupob.empapp.converter.LoginUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.entity.UsuarioEmpleado;
import org.grupob.empapp.entity.maestras.MotivoBloqueo;
import org.grupob.empapp.repository.UsuarioEmpleadoRepository;
import org.grupob.empapp.repository.maestras.MotivoBloqueoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioEmpleadoServiceImp {
    private final UsuarioEmpleadoRepository usuarioEmpRepo;
    private final MotivoBloqueoRepository motivoBloqueoRepo;
    private final LoginUsuarioEmpleadoConverter loginUsuarioEmpConvert;
    private static final int INTENTOS_MAXIMOS = 3;

    public UsuarioEmpleadoServiceImp(UsuarioEmpleadoRepository usuarioEmpRepo, MotivoBloqueoRepository motivoBloqueoRepo, LoginUsuarioEmpleadoConverter loginUsuarioEmpConvert) {
        this.usuarioEmpRepo = usuarioEmpRepo;
        this.motivoBloqueoRepo = motivoBloqueoRepo;
        this.loginUsuarioEmpConvert = loginUsuarioEmpConvert;
    }

    public LoginUsuarioEmpleadoDTO login(LoginUsuarioEmpleadoDTO dto) {
        Optional<UsuarioEmpleado> usuarioEmpOpt = usuarioEmpRepo.findByCorreo(dto.getCorreo());

        if (usuarioEmpOpt.isEmpty()) {
            throw new RuntimeException("No existe un usuario con ese correo");
        }

        UsuarioEmpleado usuario = usuarioEmpOpt.get();

        // Comprobar si el usuario está bloqueado
        if (usuario.getFechaDesbloqueo() != null && usuario.getFechaDesbloqueo().isAfter(LocalDateTime.now())) {
            return crearDTOBloqueado(usuario, "Cuenta bloqueada hasta: " + usuario.getFechaDesbloqueo());
        }

        // Verificación de contraseña
        if (!usuario.getClave().equals(dto.getClave())) {
            manejarIntentoFallido(usuario);
            return crearDTOBloqueado(usuario, "Contraseña incorrecta.");
        }

        // Login exitoso: resetear campos de bloqueo
        usuario.setIntentosSesionFallidos(0);
        usuario.setMotivoBloqueo(null);
        usuario.setFechaDesbloqueo(null);

        usuario.setUltimaConexion(LocalDateTime.now());
        usuario.setNumeroAccesos(usuario.getNumeroAccesos() + 1);

        usuarioEmpRepo.save(usuario);

        return loginUsuarioEmpConvert.convertirADTO(usuario);
    }

    private void manejarIntentoFallido(UsuarioEmpleado usuario) {
        int intentos = usuario.getIntentosSesionFallidos() + 1;
        usuario.setIntentosSesionFallidos(intentos);

        if (intentos >= 3) {
            MotivoBloqueo motivo = motivoBloqueoRepo.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Motivo de bloqueo no encontrado"));

            // Establecer motivo de bloqueo y la fecha de desbloqueo
            usuario.setMotivoBloqueo(motivo);
            usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(motivo.getMinutos()));
        }
        // Guardar los cambios en la base de datos
        usuarioEmpRepo.save(usuario);
    }

    private LoginUsuarioEmpleadoDTO crearDTOBloqueado(UsuarioEmpleado usuario, String mensaje) {
        LoginUsuarioEmpleadoDTO dto = loginUsuarioEmpConvert.convertirADTO(usuario);
        dto.setBloqueado(true);
        dto.setMensajeBloqueo(mensaje);
        if (usuario.getMotivoBloqueo() != null) {
            dto.setMotivoBloqueo(usuario.getMotivoBloqueo().getMotivo());
        }
        return dto;
    }
}
