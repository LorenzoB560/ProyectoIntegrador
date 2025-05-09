package org.grupob.adminapp.service;

import org.grupob.adminapp.dto.ProveedorDTO;
import java.util.List;

public interface ProveedorService {
    List<ProveedorDTO> findAll(); // Método para obtener todos los proveedores
    // Puedes añadir otros métodos si los necesitas
}