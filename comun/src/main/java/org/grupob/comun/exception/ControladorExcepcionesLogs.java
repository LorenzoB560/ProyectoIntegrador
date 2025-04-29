package org.grupob.comun.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControladorExcepcionesLogs {
    private static final Logger logger = LoggerFactory.getLogger(ControladorExcepcionesLogs.class);

    @ExceptionHandler({
            ClaveIncorrectaException.class,
            CredencialesInvalidasException.class,
            CuentaBloqueadaException.class,
            DepartamentoNoEncontradoException.class,
            UsuarioNoEncontradoException.class,
            UsuarioYaExisteException.class
    })
    public ResponseEntity<String> handleCustomExceptions(RuntimeException ex) {
        logger.warn("Excepción controlada:", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
