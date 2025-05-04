package org.grupob.comun.exception;

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
    public ResponseEntity<InformacionExcepcion> manejaMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        info.setTimestamp(LocalDateTime.now());
        info.setStatus(HttpStatus.BAD_REQUEST.value());
        info.setMessage(ex.getMessage());

        List<String> listaErrores = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            listaErrores.add(error.getDefaultMessage());
        });
        info.setListaErrores(listaErrores);

        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<InformacionExcepcion> manejaUsuarioYaExisteException(UsuarioYaExisteException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        info.setTimestamp(LocalDateTime.now());
        info.setStatus(HttpStatus.CONFLICT.value());
        info.setMessage(ex.getMessage());
        List<String> listaErrores = new ArrayList<>();
        listaErrores.add(ex.getMessage());
        info.setListaErrores(listaErrores);

        return new ResponseEntity<>(info, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<InformacionExcepcion> manejaEmpleadoNoEncontradoException(EmpleadoNoEncontradoException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        info.setTimestamp(LocalDateTime.now());
        info.setStatus(HttpStatus.NOT_FOUND.value());
        info.setMessage(ex.getMessage());
        List<String> listaErrores = new ArrayList<>();
        listaErrores.add(ex.getMessage());
        info.setListaErrores(listaErrores);

        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CargaMasivaException.class)
    public ResponseEntity<InformacionExcepcion> manejaCargaMasivaException(CargaMasivaException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        info.setTimestamp(LocalDateTime.now());
        info.setStatus(HttpStatus.BAD_REQUEST.value());
        info.setMessage(ex.getMessage());
        List<String> listaErrores = new ArrayList<>();
        listaErrores.add(ex.getMessage());
        info.setListaErrores(listaErrores);
        info.setError(HttpStatus.BAD_REQUEST.name());

        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String manejaHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        return "redirect:/empapp/login";
    }
}
