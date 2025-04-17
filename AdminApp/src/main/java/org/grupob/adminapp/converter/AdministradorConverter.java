package org.grupob.adminapp.converter;

//import org.grupob.empapp.dto.AltaEmpleadoDTO;
//import org.grupob.empapp.entity.Empleado;
import org.grupob.adminapp.dto.AltaEmpleadoDTO;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.entity.Administrador;
import org.grupob.adminapp.entity.Empleado;
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
