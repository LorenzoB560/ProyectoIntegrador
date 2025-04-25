package org.grupob.empapp.exception;

import java.time.LocalDateTime;

public class CuentaBloqueadaException extends RuntimeException {
    private final LocalDateTime fechaDesbloqueo;

    public CuentaBloqueadaException(String mensaje, LocalDateTime fechaDesbloqueo) {
        super(mensaje);
        this.fechaDesbloqueo = fechaDesbloqueo;
    }

    public LocalDateTime getFechaDesbloqueo() {
        return fechaDesbloqueo;
    }
}
