package org.grupob.comun.controller;

import org.grupob.comun.exception.CredencialesInvalidasException;
import org.grupob.comun.exception.InformacionExcepcion;
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
public class LoginControllerAdvice {
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<InformacionExcepcion> manejaCredencialesInvalidasException(CredencialesInvalidasException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        info.setTimestamp(LocalDateTime.now());
        info.setStatus(HttpStatus.BAD_REQUEST.value());
        info.setMessage(ex.getMessage());
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String manejaHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        InformacionExcepcion info = new InformacionExcepcion();
        return "redirect:/login";
    }
}
