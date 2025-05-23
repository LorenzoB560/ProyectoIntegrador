package org.grupob.adminapp.service;

import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.converter.ProveedorConverter; // Convertidor de AdminApp
import org.grupob.adminapp.dto.ProveedorDTO;          // DTO de AdminApp
import org.grupob.comun.entity.maestras.Proveedor;     // Entidad de comun
import org.grupob.comun.repository.maestras.ProveedorRepository; // Repositorio de comun
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImp implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorConverter proveedorConverter;

    @Override
    public List<ProveedorDTO> findAll() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return proveedores.stream()
                .map(proveedorConverter::entityToDTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList());
    }
}