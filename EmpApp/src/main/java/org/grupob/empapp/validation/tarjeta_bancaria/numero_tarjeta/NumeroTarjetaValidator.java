package org.grupob.empapp.validation.tarjeta_bancaria.numero_tarjeta;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumeroTarjetaValidator implements ConstraintValidator<NumeroTarjetaValido, String> {

    @Override
    public boolean isValid(String numeroTarjeta, ConstraintValidatorContext context) {
        // Si es nulo o vacío, deja que @NotNull y @NotBlank se encarguen
        if (numeroTarjeta == null || numeroTarjeta.isEmpty()) {
            return true;
        }

        // Verifica que tenga exactamente 16 caracteres y sean todos dígitos
        if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d{16}")) {
            return false;
        }

        // Implementación del algoritmo de Luhn
        return validarAlgoritmoLuhn(numeroTarjeta);
    }
    /**
     * Valida un número de tarjeta usando el algoritmo de Luhn.
     *
     * El algoritmo de Luhn:
     * 1. Empezando desde el dígito más a la derecha y moviéndose hacia la izquierda,
     *    duplica el valor de cada segundo dígito.
     * 2. Si el resultado de la duplicación es mayor que 9, resta 9 del resultado.
     * 3. Suma todos los dígitos.
     * 4. Si el módulo 10 del total es 0, entonces el número es válido.
     *
     * @param numeroTarjeta El número de tarjeta a validar
     * @return true si el número cumple con el algoritmo de Luhn, false en caso contrario
     */
    private boolean validarAlgoritmoLuhn(String numeroTarjeta) {
        int suma = 0;
        boolean alternar = false;

        // Recorremos de derecha a izquierda
        for (int i = numeroTarjeta.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numeroTarjeta.substring(i, i + 1));

            if (alternar) {
                n *= 2;
                if (n > 9) {
                    n = n - 9;
                }
            }

            suma += n;
            alternar = !alternar;
        }

        // El número es válido si la suma es múltiplo de 10
        return (suma % 10 == 0);
    }
}