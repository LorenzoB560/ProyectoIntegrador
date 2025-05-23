package org.grupob.adminapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.UsuarioEmpleado;
import org.grupob.comun.entity.maestras.MotivoBloqueo;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.UsuarioEmpleadoRepository;
import org.grupob.comun.repository.maestras.MotivoBloqueoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioEmpleadoServiceImp implements UsuarioEmpleadoService {

    private final UsuarioEmpleadoRepository usuarioEmpleadoRepository;
    private final EmpleadoRepository empleadoRepository; // Para buscar por ID de empleado
    private final MotivoBloqueoRepository motivoBloqueoRepository;

    @Transactional
    public void bloquearEmpleado(String empleadoId, Long motivoId) {
        Empleado empleado = empleadoRepository.findById(UUID.fromString(empleadoId))
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado: " + empleadoId));

        UsuarioEmpleado usuario = empleado.getUsuario();
        if (usuario == null) {
            throw new IllegalStateException("El empleado no tiene un usuario asociado.");
        }

        MotivoBloqueo motivo = motivoBloqueoRepository.findById(motivoId)
                .orElseThrow(() -> new EntityNotFoundException("Motivo de bloqueo no encontrado: " + motivoId));

        usuario.setMotivoBloqueo(motivo);
        usuario.setActivo(false); // Marcar como inactivo al bloquear
        usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(motivo.getMinutos())); // Limpiar fecha de desbloqueo al establecer un nuevo bloqueo


        usuarioEmpleadoRepository.save(usuario);
    }

    @Transactional
    public void desbloquearEmpleado(String empleadoId) {
        Empleado empleado = empleadoRepository.findById(UUID.fromString(empleadoId))
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado: " + empleadoId));

        UsuarioEmpleado usuario = empleado.getUsuario();
        if (usuario == null) {
            throw new IllegalStateException("El empleado no tiene un usuario asociado.");
        }

        usuario.setMotivoBloqueo(null);
        usuario.setActivo(true); // Marcar como activo al desbloquear
        usuario.setFechaDesbloqueo(null); // Opcional: limpiar fecha de desbloqueo
        usuarioEmpleadoRepository.save(usuario);
    }
}
