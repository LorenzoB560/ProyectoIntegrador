package org.grupob.comun.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.grupob.comun.converter.EmpleadoConverter;
import org.grupob.comun.converter.NominaConverter;
import org.grupob.comun.dto.*;
import org.grupob.comun.entity.LineaNomina;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.entity.maestras.Propiedad;
import org.grupob.comun.exception.EmpleadoNoEncontradoException;
import org.grupob.comun.exception.NominaNoExistenteException;
import org.grupob.comun.exception.NominaPasadaException;
import org.grupob.comun.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class NominaServiceImp implements NominaService{

    private final NominaRepository nominaRepository;
    private final NominaConverter nominaConverter;
    private final EmpleadoRepository empleadoRepository;
    private final LineaNominaRepository lineaNominaRepository;
    private final ConceptoRepository conceptoRepository;
    private final EmpleadoConverter empleadoConverter;
    private final PropiedadRepository propiedadRepository;

    public NominaServiceImp(NominaRepository nominaRepository, NominaConverter nominaConverter, EmpleadoRepository empleadoRepository, LineaNominaRepository lineaNominaRepository, ConceptoRepository conceptoRepository, EmpleadoConverter empleadoConverter, PropiedadRepository propiedadRepository) {
        this.nominaRepository = nominaRepository;
        this.nominaConverter = nominaConverter;
        this.empleadoRepository = empleadoRepository;
        this.lineaNominaRepository = lineaNominaRepository;
        this.conceptoRepository = conceptoRepository;
        this.empleadoConverter = empleadoConverter;
        this.propiedadRepository = propiedadRepository;
    }
    public NominaDTO devolverNominaPorId(UUID id){
        Optional<Nomina> nomina = nominaRepository.findById(id);
        if (nomina.isPresent()) {
            //            Optional<Empleado> empleado = empleadoRepository.findById(nominaDTO.getIdEmpleado());
//            empleado.ifPresent(value -> nominaDTO.setNombre(value.getNombre() + " " + value.getApellido()));
            return nominaConverter.convierteANominaDTO(nomina.get());
        } else{
            throw new EntityNotFoundException("La nómina seleccionada no existe");
        }
    }

    public List<NominaDTO> devolverNominas(){
        List<Nomina> nominas = nominaRepository.findAll();
        List<NominaDTO> nominasDTO = nominas.stream()
                .map(nominaConverter::convierteANominaDTO)
                .toList();


//        nominasDTO = nominasDTO.stream()
//                .peek(nomina -> {
//                    Optional<Empleado> empleado = empleadoRepository.findById(nomina.getIdEmpleado());
//                    empleado.ifPresent(value -> nomina.setNombre(value.getNombre() + " " + value.getApellido()));
//                }).toList();

        System.out.println(nominasDTO);
        return nominasDTO;
    }
    @Transactional
    public void eliminarNomina(UUID idNomina){
        Optional<Nomina> nomina = nominaRepository.findById(idNomina);
        if (nomina.isPresent()) {
            //Verificar que la nómina no sea pasada
            verificarNominaPasada(nomina);
            nominaRepository.delete(nomina.get());
        } else {
            throw new EntityNotFoundException("La nomina seleccionada no existe");
        }
    }

    @Transactional
    public void eliminarConcepto(UUID idNomina, UUID idConcepto) {
        Optional<Nomina> nomina = nominaRepository.findById(idNomina);
        if (nomina.isPresent()) {
            if (nomina.get().getPeriodo().getFechaFin().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una nómina de un mes anterior");
            }
        }

        // Solo se eliminará el concepto dentro de la nómina específica
        lineaNominaRepository.deleteLineaNominaByNominaIdAndConceptoId(idNomina, idConcepto);

        BigDecimal totalIngresos = lineaNominaRepository.getTotalIngresos(idNomina);
        BigDecimal totalDeducciones = lineaNominaRepository.getTotalDeducciones(idNomina);
        BigDecimal totalLiquido = totalIngresos.subtract(totalDeducciones);

        nominaRepository.updateTotalLiquido(idNomina, totalLiquido);
    }


    public byte[] generarPdfNomina(UUID idNomina) {
        try {
            NominaDTO nomina = devolverNominaPorId(idNomina);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("NÓMINA", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // Datos generales
            document.add(new Paragraph("Empleado: " + nomina.getNombre()));
            document.add(new Paragraph("Periodo de inicio: " + nomina.getPeriodo().getFechaInicio()));
            document.add(new Paragraph("Periodo de finalización: " + nomina.getPeriodo().getFechaFin()));
            document.add(Chunk.NEWLINE);

            // Tabla de conceptos
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            Font encabezado = new Font(Font.HELVETICA, 12, Font.BOLD);
            table.addCell(new Phrase("Concepto", encabezado));
            table.addCell(new Phrase("Porcentaje (%)", encabezado));
            table.addCell(new Phrase("Cantidad (€)", encabezado));

//            for (LineaNominaDTO linea : nomina.getLineaNominas()) {
//                table.addCell(linea.getNombreConcepto());
//                if (linea.getPorcentaje()!= null){
//                table.addCell(String.format("%.2f", linea.getPorcentaje()));
//                } else{
//                    table.addCell("");
//                }
//                table.addCell(String.format("%.2f", linea.getCantidad()));
//            }
            nomina.getLineaNominas()
                            .forEach(n -> {
                                table.addCell(n.getNombreConcepto());
                                table.addCell(n.getPorcentaje() != null ? String.format("%.2f", n.getPorcentaje()) : "");
                                table.addCell(String.format("%.2f", n.getCantidad()));
                            });

            document.add(table);

            // Total
            Paragraph total = new Paragraph("Total líquido: " + String.format("%.2f", nomina.getTotalLiquido()) + " €");
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF de la nómina", e);
        }
    }

    public List<Integer> devolverMeses(){
        return IntStream.rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toList());
    }
    public List<Integer> devolverAnios(){
        return IntStream.rangeClosed(2023, 2026)
                .boxed()
                .collect(Collectors.toList());
    }

    public List<Concepto> devolverConceptos(){
        return conceptoRepository.findAll();
    }

    public Page<NominaDTO> obtenerNominasFiltradas(FiltroNominaDTO filtro, int page) {
        Pageable pageable = PageRequest.of(page, 10); // o el tamaño que necesites

        Page<Nomina> paginaNominas = nominaRepository.buscarNominasFiltradas(
                filtro.getFiltroNombre(),
                filtro.getFechaInicio(),
                filtro.getFechaFin(),
                pageable
        );

        return getNominaDTOS(paginaNominas);
    }

//    public Page<NominaDTO> obtenerNominasFiltradasPorEmpleado(FiltroNominaEmpleadoDTO filtro, int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//
//        Page<Nomina> paginaNominas = nominaRepository.buscarNominasFiltradasPorEmpleado(
//                filtro.getIdEmpleado(), // Nuevo parámetro para filtrar por empleado
//                filtro.getFiltroMes(),
//                filtro.getFiltroAnio(),
//                filtro.getTotalLiquidoMinimo(),
//                filtro.getTotalLiquidoMaximo(),
//                filtro.getConceptosSeleccionados(),
//                pageable
//        );
//
//        return getNominaDTOS(paginaNominas);
//    }




    public Page<NominaDTO> obtenerTodasNominasPaginadas(int page, int tamanio) {
        Pageable pageable = PageRequest.of(page, tamanio); // puedes ajustar el tamaño

        Page<Nomina> paginaNominas = nominaRepository.findAll(pageable);

        return getNominaDTOS(paginaNominas);
    }
    public Page<NominaDTO> obtenerNominasEmpleado(int page, int tamanio, UUID id) {
        Pageable pageable = PageRequest.of(page, tamanio); // puedes ajustar el tamaño

        Page<Nomina> paginaNominas = nominaRepository.findNominaByEmpleadoId(id, pageable);

        return getNominaDTOS(paginaNominas);
    }

    private Page<NominaDTO> getNominaDTOS(Page<Nomina> paginaNominas) {
        return paginaNominas.map(n -> {
            String nombreEmpleado = n.getEmpleado().getNombre() + " " + n.getEmpleado().getApellido();



            List<LineaNominaDTO> lineaDTOs = n.getLineaNominas().stream().map(ln ->
                    new LineaNominaDTO(
                            ln.getConcepto().getId(),
                            ln.getConcepto().getNombre(),
                            ln.getConcepto().getTipo(),
                            ln.getCantidad(),
                            ln.getPorcentaje()
                    )
            ).toList();
            PeriodoDTO periodoDTO = new PeriodoDTO(n.getPeriodo().getFechaInicio(), n.getPeriodo().getFechaFin());
            NominaDTO nominaDTO = new NominaDTO(
                    n.getId(),
                    n.getEmpleado().getId(),
                    nombreEmpleado,
                    periodoDTO,
                    n.getTotalLiquido(),
                    lineaDTOs);

            nominaDTO = actualizarLiquidoTotal(nominaDTO);
            return nominaDTO;
        });
    }
    public void modificarNomina(NominaDTO nominaDTO){
        Nomina nomina = nominaConverter.nominaDTOConvierteAEntidad(nominaDTO);
        System.err.println(nominaDTO);
        asignarLineasNomina(nomina, nominaDTO.getLineaNominas(), conceptoRepository);
        nominaRepository.save(nomina);
    }
    public void verificarNominaPasada(NominaDTO nomina){
        if (nomina.getPeriodo().getFechaFin().isBefore(LocalDate.now())) {
            throw new NominaPasadaException("No se puede modificar una nómina de un mes pasado");
        }
    }
    private void asignarLineasNomina(Nomina nomina, List<LineaNominaDTO> lineaNominas, ConceptoRepository conceptoRepository) {
        Set<LineaNomina> lineas = lineaNominas.stream().map(lineaDTO -> {
            Concepto concepto = conceptoRepository.findById(lineaDTO.getIdConcepto())
                    .orElseThrow(() -> new RuntimeException("Concepto no encontrado: " + lineaDTO.getIdConcepto()));
            LineaNomina linea = new LineaNomina();
            linea.setConcepto(concepto);
            linea.setCantidad(lineaDTO.getCantidad());
            linea.setNomina(nomina);
            linea.setPorcentaje(lineaDTO.getPorcentaje());
            return linea;
        }).collect(Collectors.toSet());

        nomina.setLineaNominas(lineas);
    }

    public String obtenerTipoPorIdConcepto(UUID idConcepto) {
        return conceptoRepository.findById(idConcepto)
                .map(Concepto::getTipo)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
    }

    public NominaDTO actualizarLiquidoTotal(NominaDTO nominaDTO) {
        nominaDTO.getLineaNominas().forEach(c -> System.out.println("Concepto: " + c.getIdConcepto() + ", Cantidad: " + c.getCantidad() + "Porcentaje:" + c.getPorcentaje()));
        BigDecimal totalIngresos = nominaDTO.getLineaNominas().stream()
                .filter(c -> "INGRESO".equals(obtenerTipoPorIdConcepto(c.getIdConcepto())))
                .map(LineaNominaDTO::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDeducciones = nominaDTO.getLineaNominas().stream()
                .filter(c -> "DEDUCCION".equals(obtenerTipoPorIdConcepto(c.getIdConcepto())))
                .map(LineaNominaDTO::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        nominaDTO.setTotalLiquido(totalIngresos.subtract(totalDeducciones));

        return nominaDTO;
    }

    private void verificarNominaPasada(Optional<Nomina> nomina){
        if (nomina.isPresent()){
            if (nomina.get().getPeriodo().getFechaFin().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una nómina de un mes anterior");
            }
        } else {
            throw new EntityNotFoundException("La nomina seleccionada no existe");
        }
    }

    public String gestionarAccesoYRedireccion(LoginAdministradorDTO adminDTO, LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO, HttpSession sesion, Model model, HttpServletRequest request) {
        int serverPort = request.getLocalPort();
        boolean esAdminApp = (serverPort == 9090);
        boolean esEmpApp = (serverPort == 8080);

        // Redirección adecuada según el módulo de origen
        if (esAdminApp && adminDTO == null) {
            return "redirect:/adminapp/login";
        } else if (esEmpApp && loginUsuarioEmpleadoDTO == null) {
            return "redirect:/empapp/login";
        } else if (esEmpApp) {
            return "redirect:/nomina/listado/" + loginUsuarioEmpleadoDTO.getId();
        }

        return null; // No hay redirección, sigue la ejecución normal
    }
    public UUID devolverIdEmpleadoPorNomina(UUID id){
        Optional<Nomina> nomina = nominaRepository.findById(id);
        if (nomina.isPresent()) {
            return nomina.get().getEmpleado().getId();
        } else throw new NominaNoExistenteException("Esta nómina no existe");
    }

    public EmpleadoNominaDTO devolverEmpleadoPorIdNomina(UUID idNomina){
        UUID idEmpleado = devolverIdEmpleadoPorNomina(idNomina);
        return empleadoRepository.findById(idEmpleado)
                .map(empleadoConverter::convierteAEmpleadoNominaDTO)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con id: " + idNomina));
    }

    public List<Propiedad> devolverPropiedades(){
        return propiedadRepository.findAll();
    }

    public BigDecimal devolverCantidadBrutaAcumulada(UUID idNomina){
        List<Nomina> listaNominas = nominaRepository.findNominasByEmpleadoId(devolverIdEmpleadoPorNomina(idNomina));
        return listaNominas.stream()
                // Convierto a cada nómina en una lista de sus nóminas
                .flatMap(nomina -> nomina.getLineaNominas().stream())
                //Busco por el tipo de concepto, en este caso si es ingreso
                .filter(linea -> linea.getConcepto().getTipo().equalsIgnoreCase("INGRESO"))
                .map(LineaNomina::getCantidad)
                //Sumo todas las cantidades una vez obtenidas
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal devolverRetencionesAcumuladas(UUID idNomina){
        List<Nomina> listaNominas = nominaRepository.findNominasByEmpleadoId(devolverIdEmpleadoPorNomina(idNomina));
        return listaNominas.stream()
                // Convierto a cada nómina en una lista de sus nóminas
                .flatMap(nomina -> nomina.getLineaNominas().stream())
                //Busco por el tipo de concepto, en este caso si es una deducción/retención
                .filter(linea -> linea.getConcepto().getTipo().equalsIgnoreCase("DEDUCCION"))
                .map(LineaNomina::getCantidad)
                //Sumo todas las cantidades una vez obtenidas
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
