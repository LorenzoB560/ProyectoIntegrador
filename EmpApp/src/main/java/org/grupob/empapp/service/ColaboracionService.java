package org.grupob.empapp.service;

import org.grupob.comun.entity.Empleado;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;

import java.util.List;
import java.util.UUID;

public interface ColaboracionService {

    void enviarSolicitudColaboracion(UUID idSolicitante, UUID idReceptor)throws Exception;
    List<SolicitudColaboracionDTO> getSolicitudesRecibidas(UUID idEmpleado);
    List<SolicitudColaboracionDTO> getSolicitudesEnviadas(UUID idEmpleado);
    void aceptarSolicitud(UUID idSolicitud, UUID idReceptorActual)throws Exception;

    void rechazarSolicitud(UUID idSolicitud, UUID idReceptorActual)throws Exception;


    List<Empleado> getOtrosEmpleados(UUID idEmpleadoActual);
}
