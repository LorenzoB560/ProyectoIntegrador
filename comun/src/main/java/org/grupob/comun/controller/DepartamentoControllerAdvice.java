package org.grupob.comun.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.grupob.comun.exception.DepartamentoNoEncontradoException;
import org.grupob.comun.exception.InformacionExcepcion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DepartamentoControllerAdvice {
    //TRATAMIENTO DE EXCEPCIONES
    // manejo de la excepcion DepartamentoNoEncontradoException
    @ExceptionHandler(value = DepartamentoNoEncontradoException.class)
    //@ResponseStatus(HttpStatus.NOT_FOUND) // devuelve el error 404
    public ResponseEntity<InformacionExcepcion> handleDepartamentoNoEncontradoException(
            DepartamentoNoEncontradoException excepcion,
            HttpServletRequest solicitudHttp
    ){
        InformacionExcepcion informacionExcepcion = new InformacionExcepcion();
        informacionExcepcion.setMessage(excepcion.getMessage()); //El departamento no existe
        informacionExcepcion.setTimestamp(LocalDateTime.now()); //Fecha actual
        informacionExcepcion.setStatus(HttpStatus.NOT_FOUND.value()); //404
        informacionExcepcion.setError(HttpStatus.NOT_FOUND.getReasonPhrase()); //NotFound
        informacionExcepcion.setPath(solicitudHttp.getServletPath()); //ruta solicitada
        return new ResponseEntity<>(informacionExcepcion, HttpStatus.NOT_FOUND);
    }
}
