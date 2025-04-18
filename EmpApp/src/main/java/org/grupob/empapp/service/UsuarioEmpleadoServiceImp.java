package org.grupob.empapp.service;

import org.grupob.empapp.converter.LoginUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.entity.UsuarioEmpleado;
import org.grupob.empapp.entity.maestras.MotivoBloqueo;
import org.grupob.empapp.repository.UsuarioEmpleadoRepository;
import org.grupob.empapp.repository.maestras.MotivoBloqueoRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioEmpleadoServiceImp {

    // Repositorio para acceso a datos de usuarios
    private final UsuarioEmpleadoRepository usuarioEmpRepo;

    // Repositorio para motivos de bloqueo (catálogo)
    private final MotivoBloqueoRepository motivoBloqueoRepo;

    // Conversor entre entidad y DTO
    private final LoginUsuarioEmpleadoConverter loginUsuarioEmpConvert;

    // Servicio de gestión de cookies
    private final CookieService cookieService;

    // Máximo de intentos fallidos permitidos
    private static final int INTENTOS_MAXIMOS = 3;

    public UsuarioEmpleadoServiceImp(
            UsuarioEmpleadoRepository usuarioEmpRepo,
            MotivoBloqueoRepository motivoBloqueoRepo,
            LoginUsuarioEmpleadoConverter loginUsuarioEmpConvert,
            CookieService cookieService
    ) {
        this.usuarioEmpRepo = usuarioEmpRepo;
        this.motivoBloqueoRepo = motivoBloqueoRepo;
        this.loginUsuarioEmpConvert = loginUsuarioEmpConvert;
        this.cookieService = cookieService;
    }

    /**
     * Valida la existencia del usuario y su estado de bloqueo
     * @param email Correo electrónico del usuario
     * @param response Objeto para gestión de cookies
     * @return DTO con datos básicos del usuario
     * @throws RuntimeException Si el usuario no existe o está bloqueado
     */
    public LoginUsuarioEmpleadoDTO validarEmail(String email, HttpServletResponse response) {
        Optional<UsuarioEmpleado> usuarioOpt = usuarioEmpRepo.findByCorreo(email);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no registrado");
        }

        UsuarioEmpleado usuario = usuarioOpt.get();

        // Verificar bloqueo temporal
        if (usuario.getFechaDesbloqueo() != null &&
                usuario.getFechaDesbloqueo().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Cuenta bloqueada hasta: " + usuario.getFechaDesbloqueo());
        }

        cookieService.actualizarCookieHistorial(response, email);
        return loginUsuarioEmpConvert.convertirADTO(usuario);
    }

    /**
     * Valida las credenciales completas del usuario
     * @param dto Objeto con credenciales (email y contraseña)
     * @param response Objeto para gestión de cookies
     * @return DTO con datos completos del usuario
     * @throws RuntimeException Si las credenciales son inválidas
     */
    public LoginUsuarioEmpleadoDTO validarCredenciales(LoginUsuarioEmpleadoDTO dto, HttpServletResponse response) {
        UsuarioEmpleado usuario = usuarioEmpRepo.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.getClave().equals(dto.getClave())) {
            manejarIntentoFallido(usuario);
            throw new RuntimeException("Contraseña incorrecta");
        }

        actualizarEstadisticasAcceso(usuario);
        cookieService.crearCookieSesion(response, usuario.getCorreo().toString());

        return loginUsuarioEmpConvert.convertirADTO(usuario);
    }

    /**
     * Actualiza las estadísticas de acceso del usuario
     */
    private void actualizarEstadisticasAcceso(UsuarioEmpleado usuario) {
        usuario.setUltimaConexion(LocalDateTime.now());
        usuario.setNumeroAccesos(usuario.getNumeroAccesos() + 1);
        usuario.setIntentosSesionFallidos(0); // Resetear contador de fallos
        usuarioEmpRepo.save(usuario);
    }

    /**
     * Gestiona los intentos fallidos de autenticación
     */
    private void manejarIntentoFallido(UsuarioEmpleado usuario) {
        int intentos = usuario.getIntentosSesionFallidos() + 1;
        usuario.setIntentosSesionFallidos(intentos);

        if (intentos >= INTENTOS_MAXIMOS) {
            MotivoBloqueo motivo = motivoBloqueoRepo.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Motivo de bloqueo no encontrado"));

            usuario.setMotivoBloqueo(motivo);
            usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(motivo.getMinutos()));
        }
        usuarioEmpRepo.save(usuario);
    }
}
