package org.grupob.empapp.service;

import org.grupob.empapp.converter.ProveedorConverter;
import org.grupob.empapp.dto.ProveedorDTO;
import org.grupob.comun.entity.maestras.Proveedor;
import org.grupob.comun.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImp implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorConverter proveedorConverter;

    // Inyección por constructor
    public ProveedorServiceImp(ProveedorRepository proveedorRepository, ProveedorConverter proveedorConverter) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorConverter = proveedorConverter;
    }

    @Override
    public List<ProveedorDTO> findAll() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return proveedores.stream()
                .map(proveedorConverter::entityToDTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList());
    }
}