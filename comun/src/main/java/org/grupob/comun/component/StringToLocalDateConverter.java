//package org.grupob.comun.component;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class StringToLocalDateConverter implements Converter<String, LocalDate> {
//
//    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
//            DateTimeFormatter.ofPattern("d/M/yy"),
//            DateTimeFormatter.ofPattern("dd/MM/yy"),
//            DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    );
//
//    @Override
//    public LocalDate convert(String source) {
//        if (source == null || source.trim().isEmpty()) {
//            return null;
//        }
//
//        // Intentar parsear con cada formato
//        for (DateTimeFormatter formatter : FORMATTERS) {
//            try {
//                return LocalDate.parse(source, formatter);
//            } catch (DateTimeParseException e) {
//                // Continúa con el siguiente formato
//            }
//        }
//
//        throw new IllegalArgumentException("No se pudo convertir la fecha: " + source);
//    }
//}