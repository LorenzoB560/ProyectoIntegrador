package org.grupob.comun.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        // Formatear fecha.
        String[] parts = source.split("/");
        if (parts.length == 3) {
            //Si el usuario pasa una fecha, ej: 1/1/2000.

            //Coge la parte del día, y si su longitud es de 1 le añade un 0 delante, si no lo deja como está
            String day = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
            //Lo mismo pero con el mes
            String month = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
            String year = parts[2];
            source = day + "/" + month + "/" + year;
        }
        return LocalDate.parse(source, formatter);
    }
}
