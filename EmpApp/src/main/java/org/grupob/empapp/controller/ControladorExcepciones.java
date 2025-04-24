package org.grupob.empapp.controller;

import org.apache.coyote.Response;
import org.grupob.empapp.dto.InformacionExcepcion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControladorExcepciones {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InformacionExcepcion> maneja(MethodArgumentNotValidException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        info.setTimestamp(LocalDateTime.now());
        info.setStatus(400);
        info.setError(ex.getClass().getName());
        List<String> listaErrores = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {listaErrores.add(error.getDefaultMessage());});
        info.setListaErrores(listaErrores);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String manejaHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        return "redirect:/empapp/login";
    }
}
