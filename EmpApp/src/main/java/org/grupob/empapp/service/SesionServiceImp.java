/*
package org.grupob.empapp.service;

import jakarta.persistence.EntityNotFoundException;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.EmpleadoDTO;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;

public class SesionServiceImp {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoConverter empleadoConverter;

    public SesionServiceImp(EmpleadoRepository empleadoRepository, EmpleadoConverter empleadoConverter) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoConverter = empleadoConverter;
    }


    public EmpleadoDTO obtenerEmpleadoPorLoginUsuario(LoginUsuarioEmpleadoDTO loginUsuario) {
        // Buscar el empleado relacionado con el usuario autenticado
        Empleado empleado = empleadoRepository.findByUsuarioId(loginUsuario.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe empleado para el usuario con id: " + loginUsuario.getId()));

        // Mapear la entidad Empleado a EmpleadoDTO



        return empleadoDTO;
    }
}
*/
