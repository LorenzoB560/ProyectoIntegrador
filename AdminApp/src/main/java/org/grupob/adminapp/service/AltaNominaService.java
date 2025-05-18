package org.grupob.adminapp.service;

import org.grupob.adminapp.dto.AltaNominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.maestras.Concepto;

import java.util.List;
import java.util.UUID;


public interface AltaNominaService {


    List<Empleado> devuelveEmpleados();
    List<String> devolverMeses();
    List<String> devolverAnios();
    List<Concepto> devolverConcepto();
    Concepto devolverSalarioBase(List<Concepto> conceptos);
    List<Concepto> devolverConceptosRestantes(List<Concepto> conceptos);
    Concepto obtenerConceptoPorId(UUID id);
    void guardarNomina(AltaNominaDTO altaNominaDTO);
}