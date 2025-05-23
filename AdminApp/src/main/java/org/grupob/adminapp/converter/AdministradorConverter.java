package org.grupob.adminapp.converter;


import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.entity.Administrador;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AdministradorConverter {
    ModelMapper modelMapper;


    public AdministradorConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LoginAdministradorDTO convertirADTO(Administrador admin) {
        return modelMapper.map(admin, LoginAdministradorDTO.class);
    }

    public Administrador convertirAEntidad(LoginAdministradorDTO dto) {
        return modelMapper.map(dto, Administrador.class);
    }
}
