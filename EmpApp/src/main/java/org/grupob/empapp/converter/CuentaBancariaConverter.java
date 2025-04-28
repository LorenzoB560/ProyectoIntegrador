package org.grupob.empapp.converter;

import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.auxiliar.CuentaBancaria;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.CuentaBancariaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CuentaBancariaConverter {
    ModelMapper modelMapper;


    public CuentaBancariaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CuentaBancaria convertirAEntidad(CuentaBancariaDTO cuentaBancariaDTO){
        CuentaBancaria cuentaBancaria = modelMapper.map(cuentaBancariaDTO, CuentaBancaria.class);

        cuentaBancaria.setIBAN(cuentaBancariaDTO.getIban());
        return cuentaBancaria;
    }
}
