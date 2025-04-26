package org.grupob.empapp.service;

import org.grupob.empapp.converter.LoginUsuarioEmpleadoConverter;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.exception.ClaveIncorrectaException;
import org.grupob.empapp.exception.CuentaBloqueadaException;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.exception.UsuarioNoEncontradoException;
import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.grupob.comun.repository.maestras.MotivoBloqueoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // Cifrador de contraseñas (para validar)
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    // Máximo de intentos fallidos permitidos
    private static final int INTENTOS_MAXIMOS = 3;

    public UsuarioEmpleadoServiceImp(
            UsuarioEmpleadoRepository usuarioEmpRepo,
            MotivoBloqueoRepository motivoBloqueoRepo,
            LoginUsuarioEmpleadoConverter loginUsuarioEmpConvert) {
        this.usuarioEmpRepo = usuarioEmpRepo;
        this.motivoBloqueoRepo = motivoBloqueoRepo;
        this.loginUsuarioEmpConvert = loginUsuarioEmpConvert;
    }

    public UsuarioEmpleado devuelveUsuarioEmpPorUsuario(String usuario){
        Optional<UsuarioEmpleado> usuarioOpt = usuarioEmpRepo.findByUsuario(usuario);

        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get();
        }
        throw new UsuarioNoEncontradoException("El usuario no está registrado");
    }

    /**
     * Valida la existencia del usuario y su estado de bloqueo
     * @param correo Correo electrónico del usuario
     * @return DTO con datos básicos del usuario
     * @throws RuntimeException Si el usuario no existe o está bloqueado
     */
    public Boolean validarEmail(String correo) {
        Optional<UsuarioEmpleado> usuarioOpt = usuarioEmpRepo.findByUsuario(correo);

        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException("El usuario no está registrado");
        }

        UsuarioEmpleado usuario = usuarioOpt.get();

        // Verificar bloqueo temporal
        if (usuario.getFechaDesbloqueo() != null &&
                usuario.getFechaDesbloqueo().isAfter(LocalDateTime.now())) {
            throw new CuentaBloqueadaException("Cuenta bloqueada por seguridad", usuario.getFechaDesbloqueo());
        }

        return true;
    }

    /**
     * Valida las credenciales completas del usuario
     * @param dto Objeto con credenciales (email y contraseña)
     * @return DTO con datos completos del usuario
     * @throws RuntimeException Si las credenciales son inválidas
     */
    public Boolean validarCredenciales(LoginUsuarioEmpleadoDTO dto) {
        UsuarioEmpleado usuarioEmp = usuarioEmpRepo.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Credenciales inválidas"));

        // Verificar bloqueo temporal
        if (usuarioEmp.getFechaDesbloqueo() != null &&
                usuarioEmp.getFechaDesbloqueo().isAfter(LocalDateTime.now())) {
            throw new CuentaBloqueadaException("Cuenta bloqueada por intentos de sesion fallidos", usuarioEmp.getFechaDesbloqueo());
        }

        if (!passwordEncoder.matches(dto.getClave(), usuarioEmp.getClave())) {
            int intentos = manejarIntentoFallido(usuarioEmp);
            int restantes = INTENTOS_MAXIMOS - intentos;

            throw new ClaveIncorrectaException("Contraseña incorrecta", restantes > 0 ? restantes : 0);
        }

        actualizarEstadisticasAcceso(usuarioEmp);
        return true;
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
    private int manejarIntentoFallido(UsuarioEmpleado usuario) {
        int intentos = usuario.getIntentosSesionFallidos() +1;
        usuario.setIntentosSesionFallidos(intentos);

        if (intentos >= INTENTOS_MAXIMOS) {
            MotivoBloqueo motivo = motivoBloqueoRepo.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Motivo de bloqueo no encontrado"));

            usuario.setMotivoBloqueo(motivo);
            usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(motivo.getMinutos()));
        }
        usuarioEmpRepo.save(usuario);
        return usuario.getIntentosSesionFallidos();
    }
}
