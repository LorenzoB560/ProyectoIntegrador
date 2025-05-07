package org.grupob.adminapp.controller;


import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;
import org.grupob.adminapp.dto.LineaNominaDTO;
import org.grupob.adminapp.dto.NominaDTO;
import org.grupob.adminapp.service.NominaServiceImp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.lowagie.text.Document;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/nomina")
public class NominaRestController {
    private final NominaServiceImp nominaServiceImp;

    public NominaRestController(NominaServiceImp nominaServiceImp) {
        this.nominaServiceImp = nominaServiceImp;
    }
    @DeleteMapping("/eliminar-nomina/{idNomina}")
    public ResponseEntity<Void> eliminarNomina(@PathVariable UUID idNomina) {
        nominaServiceImp.eliminarNomina(idNomina);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/eliminar-concepto/{idNomina}/{idConcepto}")
    public ResponseEntity<Void> eliminarConcepto(@PathVariable UUID idNomina, @PathVariable UUID idConcepto) {
        nominaServiceImp.eliminarConcepto(idNomina, idConcepto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/descargar-pdf/{id}")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable UUID id) {
        try {
            byte[] pdfBytes = nominaServiceImp.generarPdfNomina(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "nomina_" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo generar el PDF", e);
        }
    }


}
