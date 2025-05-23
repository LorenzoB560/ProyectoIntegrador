package org.grupob.empapp.service;

import lombok.RequiredArgsConstructor;
import org.grupob.empapp.converter.ProveedorConverter;
import org.grupob.empapp.dto.ProveedorDTO;
import org.grupob.comun.entity.maestras.Proveedor;
import org.grupob.comun.repository.maestras.ProveedorRepository;
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