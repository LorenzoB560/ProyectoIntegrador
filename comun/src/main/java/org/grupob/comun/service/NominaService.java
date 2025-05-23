package org.grupob.comun.service;

import org.grupob.comun.dto.*;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.entity.maestras.Propiedad;
import org.grupob.comun.repository.ConceptoRepository;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface NominaService {

    NominaDTO devolverNominaPorId(UUID id);
    List<NominaDTO> devolverNominas();
    void eliminarNomina(UUID idNomina);
    void eliminarConcepto(UUID idNomina, UUID idConcepto);
    byte[] generarPdfNomina(UUID idNomina);
    List<Integer> devolverMeses();
    List<Integer> devolverAnios();
    List<Concepto> devolverConceptos();
    Page<NominaDTO> obtenerNominasFiltradas(FiltroNominaDTO filtro, int page);
    Page<NominaDTO> obtenerNominasFiltradasPorEmpleado(FiltroNominaEmpleadoDTO filtro, int page);
    Page<NominaDTO> obtenerTodasNominasPaginadas(int page, int tamanio);
    Page<NominaDTO> obtenerNominasEmpleado(int page, int tamanio, UUID id);
    void modificarNomina(NominaDTO nominaDTO);
    void verificarNominaPasada(NominaDTO nomina);
    String obtenerTipoPorIdConcepto(UUID idConcepto);
    NominaDTO actualizarLiquidoTotal(NominaDTO nominaDTO);
    String gestionarAccesoYRedireccion(LoginAdministradorDTO adminDTO);
    EmpleadoNominaDTO devolverEmpleadoPorIdNomina(UUID idNomina);
    List<Propiedad> devolverPropiedades();
    BigDecimal devolverCantidadBrutaAcumulada(UUID idNomina);
    BigDecimal devolverRetencionesAcumuladas(UUID idNomina);
    void asignarDatosComunesNomina(UUID id, Model model);

}
