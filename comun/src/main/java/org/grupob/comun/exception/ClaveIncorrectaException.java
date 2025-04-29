package org.grupob.comun.exception;

public class ClaveIncorrectaException extends RuntimeException {
    private final int intentosRestantes;

    public ClaveIncorrectaException(String mensaje, int intentosRestantes) {
        super(mensaje);
        this.intentosRestantes = intentosRestantes;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
