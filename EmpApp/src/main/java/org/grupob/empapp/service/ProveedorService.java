package org.grupob.empapp.service;

import org.grupob.empapp.dto.ProveedorDTO;

import java.util.List;

public interface ProveedorService {
    List<ProveedorDTO> findAll(); // Método para obtener todos los proveedores
    // Puedes añadir otros métodos si los necesitas
}