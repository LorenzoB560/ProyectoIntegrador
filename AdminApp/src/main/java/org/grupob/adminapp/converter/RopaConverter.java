package org.grupob.adminapp.converter;


import org.grupob.adminapp.dto.masiva.RopaCargaDTO;
import org.grupob.comun.entity.Ropa;
import org.grupob.comun.entity.maestras.Talla;
import org.grupob.comun.repository.maestras.TallaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RopaConverter {

    private final ModelMapper modelMapper;
    private final TallaRepository tallaRepository;

    public RopaConverter(ModelMapper modelMapper, TallaRepository tallaRepository) {
        this.modelMapper = modelMapper;
        this.tallaRepository = tallaRepository;
    }

    public RopaCargaDTO convertirADto(Ropa entidad) {
        return modelMapper.map(entidad, RopaCargaDTO.class);
    }

    public Ropa convertirAEntidad(RopaCargaDTO dto) {
        Ropa ropa = modelMapper.map(dto, Ropa.class);

        if (dto.getTallas() != null && !dto.getTallas().isEmpty()) {
            List<Talla> tallas = dto.getTallas().stream()
                    .map(tallaNombre -> tallaRepository.findByTalla(tallaNombre)
                            .orElseThrow(() -> new IllegalArgumentException("Talla no existe: " + tallaNombre)))
                    .toList();
            ropa.setTallas(tallas);
        }

        return ropa;
    }
}
