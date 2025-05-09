package org.grupob.adminapp.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.grupob.adminapp.converter.NominaConverter;
import org.grupob.adminapp.dto.FiltroNominaDTO;
import org.grupob.adminapp.dto.LineaNominaDTO;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.entity.Nomina;
import org.grupob.comun.entity.maestras.Concepto;
import org.grupob.comun.repository.ConceptoRepository;
import org.grupob.comun.repository.EmpleadoRepository;
import org.grupob.comun.repository.LineaNominaRepository;
import org.grupob.comun.repository.NominaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class NominaServiceImp implements NominaService{

    private final NominaRepository nominaRepository;
    private final NominaConverter nominaConverter;
    private final EmpleadoRepository empleadoRepository;
    private final LineaNominaRepository lineaNominaRepository;
    private final ConceptoRepository conceptoRepository;

    public NominaServiceImp(NominaRepository nominaRepository, NominaConverter nominaConverter, EmpleadoRepository empleadoRepository, LineaNominaRepository lineaNominaRepository, ConceptoRepository conceptoRepository) {
        this.nominaRepository = nominaRepository;
        this.nominaConverter = nominaConverter;
        this.empleadoRepository = empleadoRepository;
        this.lineaNominaRepository = lineaNominaRepository;
        this.conceptoRepository = conceptoRepository;
    }
    public NominaDTO devolverNominaPorId(UUID id){
        Optional<Nomina> nomina = nominaRepository.findById(id);
        if (nomina.isPresent()) {
            //            Optional<Empleado> empleado = empleadoRepository.findById(nominaDTO.getIdEmpleado());
//            empleado.ifPresent(value -> nominaDTO.setNombre(value.getNombre() + " " + value.getApellido()));
            return nominaConverter.convierteADTO(nomina.get());
        } else{
            throw new EntityNotFoundException("La nómina seleccionada no existe");
        }
    }

    public List<NominaDTO> devolverNominas(){
        List<Nomina> nominas = nominaRepository.findAll();
        List<NominaDTO> nominasDTO = nominas.stream()
                .map(nominaConverter::convierteADTO)
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
            YearMonth mesNomina = YearMonth.of(nomina.get().getAnio(), nomina.get().getMes());
            YearMonth mesActual = YearMonth.now();
            if (mesNomina.isBefore(mesActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una nómina de un mes anterior");
            }
            nominaRepository.delete(nomina.get());
        } else {
            throw new EntityNotFoundException("La nomina seleccionada no existe");
        }
    }

    @Transactional
    public void eliminarConcepto(UUID idNomina, UUID idConcepto) {
        Optional<Nomina> nomina = nominaRepository.findById(idNomina);
        if (nomina.isPresent()) {
            YearMonth mesNomina = YearMonth.of(nomina.get().getAnio(), nomina.get().getMes());
            YearMonth mesActual = YearMonth.now();
            if (mesNomina.isBefore(mesActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una nómina de un mes anterior");
            }
        }

        lineaNominaRepository.deleteLineaNominaByConceptoId(idConcepto);

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
            document.add(new Paragraph("Mes: " + nomina.getMes() + " / Año: " + nomina.getAnio()));
            document.add(Chunk.NEWLINE);

            // Tabla de conceptos
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            Font encabezado = new Font(Font.HELVETICA, 12, Font.BOLD);
            table.addCell(new Phrase("Concepto", encabezado));
            table.addCell(new Phrase("Cantidad (€)", encabezado));

            for (LineaNominaDTO linea : nomina.getLineaNominas()) {
                table.addCell(linea.getNombreConcepto());
                table.addCell(String.format("%.2f", linea.getCantidad()));
            }

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
                filtro.getFiltroMes(),
                filtro.getFiltroAnio(),
                filtro.getTotalLiquidoMinimo(),
                filtro.getTotalLiquidoMaximo(),
                filtro.getConceptosSeleccionados(),
                pageable
        );

        return getNominaDTOS(paginaNominas);
    }


    public Page<NominaDTO> obtenerTodasNominasPaginadas(int page, int tamanio) {
        Pageable pageable = PageRequest.of(page, tamanio); // puedes ajustar el tamaño

        Page<Nomina> paginaNominas = nominaRepository.findAll(pageable);

        return getNominaDTOS(paginaNominas);
    }

    private Page<NominaDTO> getNominaDTOS(Page<Nomina> paginaNominas) {
        return paginaNominas.map(n -> {
            String nombreEmpleado = n.getEmpleado().getNombre() + " " + n.getEmpleado().getApellido();

            List<LineaNominaDTO> lineaDTOs = n.getLineaNominas().stream().map(ln ->
                    new LineaNominaDTO(
                            ln.getConcepto().getId(),
                            ln.getConcepto().getNombre(),
                            ln.getCantidad()
                    )
            ).toList();

            return new NominaDTO(
                    n.getId(),
                    n.getEmpleado().getId(),
                    nombreEmpleado,
                    n.getMes(),
                    n.getAnio(),
                    n.getTotalLiquido(),
                    lineaDTOs
            );
        });
    }


}
