package org.grupob.empapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CookieService {
    /**
     * Valida que la cookie tenga el formato esperado: "nombre!numero/nombre!numero".
     * No puede haber doble separador, valores vacíos, ni símbolos mal colocados.
     */
    public static boolean cookieValida(String cookieValue) {
        if (cookieValue == null || cookieValue.isEmpty()) return false;

        // Expresión regular para validar estructura esperada.
        String regex = "^(?!.*//)(?!.*!!)(?!.*!$)(?!^!)([a-zA-Z0-9]+!\\d+)(/[a-zA-Z0-9]+!\\d+)*$";
        return cookieValue.matches(regex);
    }

    /**
     * Convierte la cadena serializada de la cookie a un Map donde:
     * clave = nombre del usuario, valor = número de accesos.
     * Si la cookie es inválida, devuelve un mapa vacío.
     */
    public static Map<String, Integer> deserializar(String cookieValue) {
        if (!cookieValida(cookieValue)) return new HashMap<>();

        Map<String, Integer> usuarios = new HashMap<>();
        for (String par : cookieValue.split("/")) {
            String[] datos = par.split("!");
            usuarios.put(datos[0], Integer.parseInt(datos[1]));
        }
        return usuarios;
    }

    /**
     * Convierte un Map de accesos por usuario a la cadena serializada para guardar en cookie.
     * Ejemplo: {"ana"=2, "juan"=4} => "ana!2/juan!4"
     */
    public static String serializar(Map<String, Integer> usuarios) {
        StringBuilder valor = new StringBuilder();
        boolean primero = true;

        for (Map.Entry<String, Integer> entry : usuarios.entrySet()) {
            if (!primero) valor.append("/"); // añadir separador entre pares
            valor.append(entry.getKey()).append("!").append(entry.getValue());
            primero = false;
        }
        return valor.toString();
    }

    /**
     * Aumenta el contador de accesos del usuario actual y genera la nueva cadena serializada.
     * Si el usuario no está presente, se añade con un valor inicial de 1.
     */
    public static String actualizar(Map<String, Integer> usuarios, String valor, String usuarioActual) {
        if (valor != null && valor.contains(usuarioActual)) {
            int contador = usuarios.getOrDefault(usuarioActual, 0);
            usuarios.put(usuarioActual, ++contador);
        } else {
            usuarios.put(usuarioActual, 1);
        }
        return serializar(usuarios);
    }

    /**
     * Crea y añade una nueva cookie al response.
     */
    public static void crearCookie(HttpServletResponse response, String nombre, String valor, int duracionSegundos) {
        Cookie cookie = new Cookie(nombre, valor);
        cookie.setPath("/"); // hace que esté disponible en toda la app
        cookie.setMaxAge(duracionSegundos); // duración de la cookie
        response.addCookie(cookie);
    }

    /**
     * Elimina una cookie del navegador del usuario, asignándole una duración 0.
     */
    public static void eliminarCookie(HttpServletResponse response, String nombre) {
        Cookie cookie = new Cookie(nombre, "");
        cookie.setPath("/");
        cookie.setMaxAge(0); // al poner 0, el navegador la borra
        response.addCookie(cookie);
    }

    /**
     * Recupera el valor de una cookie concreta desde el request.
     * Devuelve null si no se encuentra la cookie con ese nombre.
     */
    public static String obtenerValorCookie(HttpServletRequest request, String nombre) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(nombre)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
