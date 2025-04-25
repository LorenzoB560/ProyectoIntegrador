package org.grupob.empapp.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String msg) {
        super(msg);
    }
}
