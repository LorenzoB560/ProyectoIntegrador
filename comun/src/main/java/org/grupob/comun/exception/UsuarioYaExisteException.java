package org.grupob.comun.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String msg) {
        super(msg);
    }
}
