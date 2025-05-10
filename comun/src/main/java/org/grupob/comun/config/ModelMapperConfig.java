package org.grupob.comun.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        // Configuración detallada del ModelMapper para manejar correctamente los DTOs y entidades
//        modelMapper.getConfiguration()
//                // Evita que valores null sobreescriban datos existentes
//                // Crucial para actualizaciones parciales de productos donde solo cambian algunos campos
//                // Ejemplo: Si unidades=0 y actualizamos solo precio, no queremos perder unidades
//                .setSkipNullEnabled(true)
//
//                // Asegura que solo se mapeen propiedades con nombres exactamente iguales
//                // Evita mapeos no deseados entre campos con nombres similares pero no idénticos
//                // Importante para objetos complejos como Dimension o campos específicos de subtipos
//                .setMatchingStrategy(MatchingStrategies.STRICT)
//
//                // Permite acceder directamente a campos privados sin necesidad de getters/setters
//                // Útil para mapear campos como fechaFabricacion o colores en muebles
//                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
//
//                // Permite el mapeo directo entre campos, no solo a través de métodos públicos
//                // Mejora el rendimiento y simplifica el mapeo de colecciones como tallas o categorías
//                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}
