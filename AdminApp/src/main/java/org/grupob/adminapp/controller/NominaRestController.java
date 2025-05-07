package org.grupob.adminapp.controller;

import jakarta.persistence.EntityNotFoundException;
import org.grupob.adminapp.service.NominaServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/nomina")
public class NominaRestController {
    private final NominaServiceImp nominaServiceImp;

    public NominaRestController(NominaServiceImp nominaServiceImp) {
        this.nominaServiceImp = nominaServiceImp;
    }

    @DeleteMapping("/eliminar-concepto/{idNomina}/{idConcepto}")
    public ResponseEntity<Void> eliminarConcepto(@PathVariable UUID idNomina, @PathVariable UUID idConcepto) {
//        try {
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el concepto", e);

        nominaServiceImp.eliminarConcepto(idNomina, idConcepto);
        return ResponseEntity.noContent().build();
    }
}
