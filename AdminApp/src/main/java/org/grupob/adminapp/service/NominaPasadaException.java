package org.grupob.adminapp.service;

import org.springframework.http.HttpStatus;

public class NominaPasadaException extends RuntimeException {
    public NominaPasadaException() {
        super();
    }
    public NominaPasadaException(String message) {
        super(message);
    }
}
