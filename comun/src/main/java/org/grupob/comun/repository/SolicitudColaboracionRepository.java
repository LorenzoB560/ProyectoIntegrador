package org.grupob.comun.repository;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.SolicitudColaboracion;
import org.grupob.comun.entity.maestras.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitudColaboracionRepository extends JpaRepository<SolicitudColaboracion, UUID> {
    List<SolicitudColaboracion> findBySolicitanteAndEstadoNombre(Empleado solicitante, String nombreEstado);
    List<SolicitudColaboracion> findByReceptorAndEstadoNombre(Empleado receptor, String nombreEstado);
    List<SolicitudColaboracion> findBySolicitante(Empleado solicitante);
    List<SolicitudColaboracion> findByReceptor(Empleado receptor);
    Optional<SolicitudColaboracion> findBySolicitanteAndReceptorAndEstado(Empleado solicitante, Empleado receptor, Estado estado);
    // Para verificar si ya existe una solicitud PENDIENTE o una colaboración activa
    Optional<SolicitudColaboracion> findBySolicitanteAndReceptorAndEstado_Nombre(Empleado solicitante, Empleado receptor, String estadoNombre);
}