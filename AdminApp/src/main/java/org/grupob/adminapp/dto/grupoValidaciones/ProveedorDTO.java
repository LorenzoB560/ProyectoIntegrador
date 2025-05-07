package org.grupob.adminapp.dto.grupoValidaciones;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class ProveedorDTO {
    private UUID id;
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;
}
