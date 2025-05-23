package org.grupob.empapp.service;

import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.comun.entity.Departamento;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.EntidadBancaria;
import org.grupob.comun.entity.maestras.*;
import org.grupob.empapp.dto.AltaEmpleadoDTO;

import java.util.List;

public interface AltaEmpleadoService {
    void guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO, String id);
    List<Genero> devolverGeneros();
    List<Pais> devolverPaises();
    List<TipoVia> devolverTipoVias();
    List<TipoDocumento> devolverTipoDocumentos();
    List<Departamento> devolverDepartamentos();
    List<EntidadBancaria> devolverEntidadesBancarias();
    List<TipoTarjetaCredito> devolverTipoTarjetasCredito();
    List<String> devolverMeses();
    List<String> devolverAnios();
    List<Empleado> devolverEmpleados();
    boolean usuarioExiste(LoginUsuarioEmpleadoDTO sesion);
    void actualizarDatos(AltaEmpleadoDTO datosNuevos, AltaEmpleadoDTO datosAnteriores);
}
