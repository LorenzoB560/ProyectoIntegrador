package org.grupob.empapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.grupob.empapp.entity.UsuarioEmpleado;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CookieService {

    /**
     * Valida el formato de una cookie según las reglas de negocio.
     * Formato válido: "usuario!contador[/usuario!contador]*"
     * @param cookieValue Valor de la cookie a validar
     * @return true si el formato es correcto, false en caso contrario
     */
    public static boolean cookieValida(String cookieValue) {
        if (cookieValue == null || cookieValue.isEmpty()) return false;

        // Expresión regular que verifica:
        // - No permite dobles separadores (// o !!)
        // - No permite terminaciones con !
        // - Formato usuario!numero repetido
        String regex = "^(?!.*//)(?!.*!!)(?!.*!$)(?!^!)([a-zA-Z0-9]+!\\d+)(/[a-zA-Z0-9]+!\\d+)*$";
        return cookieValue.matches(regex);
    }

    /**
     * Convierte el valor de la cookie en un mapa de usuarios con sus contadores
     * @param cookieValue Valor de la cookie a deserializar
     * @return Mapa de <Usuario, Contador> o mapa vacío si es inválido
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
     * Serializa un mapa de usuarios a formato de cadena para cookies
     * @param usuarios Mapa con usuarios y contadores
     * @return Cadena en formato "usuario!contador[/usuario!contador]*"
     */
    public static String serializar(Map<String, Integer> usuarios) {
        StringBuilder valor = new StringBuilder();
        boolean primero = true;

        for (Map.Entry<String, Integer> entry : usuarios.entrySet()) {
            if (!primero) valor.append("/");
            valor.append(entry.getKey()).append("!").append(entry.getValue());
            primero = false;
        }
        return valor.toString();
    }

    /**
     * Actualiza el contador de accesos para un usuario específico
     * @param usuarios Mapa actual de usuarios
     * @param valor Valor actual de la cookie
     * @param usuarioActual Usuario a actualizar
     * @return Nuevo valor serializado para la cookie
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
     * Crea una nueva cookie con configuración de seguridad básica
     */
    public static void crearCookie(HttpServletResponse response, String nombre, String valor, int duracionSegundos) {
        Cookie cookie = new Cookie(nombre, valor);
        cookie.setPath("/"); // Accesible en toda la aplicación
        cookie.setMaxAge(duracionSegundos); // Duración en segundos
        response.addCookie(cookie);
    }

    /**
     * Elimina una cookie del cliente
     * @param response Objeto HttpServletResponse
     * @param nombre Nombre de la cookie a eliminar
     */
    public static void eliminarCookie(HttpServletResponse response, String nombre) {
        Cookie cookie = new Cookie(nombre, "");
        cookie.setPath("/");
        cookie.setMaxAge(0); // Tiempo de vida cero para eliminación
        response.addCookie(cookie);
    }

    /**
     * Obtiene el valor de una cookie específica
     * @param request Objeto HttpServletRequest
     * @param nombre Nombre de la cookie a buscar
     * @return Valor de la cookie o null si no existe
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

    /**
     * Actualiza el historial de logins exitosos en cookies
     */
    public void actualizarCookieHistorial(HttpServletResponse response, String email) {
        String valorActual = obtenerValorCookie(null, "historialLogins");
        Map<String, Integer> usuarios = deserializar(valorActual);
        String nuevoValor = actualizar(usuarios, valorActual, email);
        crearCookie(response, "historialLogins", nuevoValor, 604800); // 7 días
    }

    /**
     * Crea la cookie de sesión activa
     */
    public void crearCookieSesion(HttpServletResponse response, String correo) {
        crearCookie(response, "sesionActiva", correo, 1800); // 30 minutos
    }


    /**
     * Maneja el proceso de cierre de sesión
     */
    public void cerrarSesion(HttpServletResponse response, String correo) {
        eliminarCookie(response, "sesionActiva");
        actualizarCookieCierreSesion(response, correo);
    }

    /**
     * Actualiza el contador de logins al cerrar sesión
     */
    private void actualizarCookieCierreSesion(HttpServletResponse response, String correo) {
        String valorActual = obtenerValorCookie(null, "historialLogins");
        Map<String, Integer> usuarios = deserializar(valorActual);

        if (usuarios.containsKey(correo)) {
            usuarios.put(correo, usuarios.get(correo) - 1);
            String nuevoValor = serializar(usuarios);
            crearCookie(response, "historialLogins", nuevoValor, 604800);
        }
    }
}
