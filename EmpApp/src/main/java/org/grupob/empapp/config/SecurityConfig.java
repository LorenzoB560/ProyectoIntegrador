//package org.grupob.empapp.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()  // Permite acceso libre a todas las URLs
//                )
////        Spring Security protege contra CSRF exigiendo un token especial en
////        las peticiones que modifican datos (como POST, PUT o DELETE)
//                .csrf(AbstractHttpConfigurer::disable);  // Deshabilitar CSRF si no lo necesitas
//
//        return http.build();
//    }
//}