package org.grupob.empapp.service;

import org.grupob.comun.entity.Empleado;
import org.grupob.empapp.dto.ColaboracionEstablecidaDTO;
import org.grupob.empapp.dto.HistorialColaboracionItemDTO;
import org.grupob.empapp.dto.SolicitudColaboracionDTO;

import java.util.List;
import java.util.UUID;

public interface ColaboracionService {


    public void finalizarPeriodoColaboracion(UUID idColaboracion, UUID idEmpleadoFinalizador) throws Exception;
    void enviarSolicitudColaboracion(UUID idSolicitante, UUID idReceptor)throws Exception;
    List<SolicitudColaboracionDTO> getSolicitudesRecibidas(UUID idEmpleado);
    List<SolicitudColaboracionDTO> getSolicitudesEnviadas(UUID idEmpleado);
    void aceptarSolicitud(UUID idSolicitud, UUID idReceptorActual)throws Exception;

    void rechazarSolicitud(UUID idSolicitud, UUID idReceptorActual)throws Exception;

    public List<HistorialColaboracionItemDTO> getHistorialCompletoColaboraciones(UUID idEmpleadoActual);

    public List<ColaboracionEstablecidaDTO> getColaboracionesEstablecidas(UUID idEmpleadoActual);

    List<Empleado> getOtrosEmpleados(UUID idEmpleadoActual);
}
