package org.grupob.empapp.service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupob.comun.entity.maestras.Propiedad;
import org.grupob.comun.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Data
/*@AllArgsConstructor
@NoArgsConstructor*/
@Service
public class CookieService {

    private final PropiedadRepository propiedadRepository;

//    @Value("${app.cookie.secret}")
    private String claveSecreta;

    private  SecretKeySpec secretKeySpec;


    public SecretKeySpec getSecretKey() {
        if (secretKeySpec == null) {
            //Si está null, obtengo la contraseña desde la base de datos, y la asigno.
            String claveSecreta = propiedadRepository.findById(4L)
                    .map(Propiedad::getValor)
                    .orElseThrow(() -> new RuntimeException("Clave no encontrada"));
            // le asigno a secretKeySpec los bytes de la clave secreta y su algoritmo.
            secretKeySpec = new SecretKeySpec(claveSecreta.getBytes(), "AES");
        }
        return secretKeySpec;
    }
//    @PostConstruct
//    @Transactional
//    void init() {
//        // Inicializa la clave después de inyectar la propiedad
//        claveSecreta = devuelveClaveCifrada();
//        secretKeySpec = new SecretKeySpec(claveSecreta.getBytes(), "AES");
//    }
//    private String devuelveClaveCifrada(){
//        System.out.println(propiedadRepository.findAll());
//        Optional<Propiedad> propiedad = propiedadRepository.findById(4L);
//        if (propiedad.isPresent()) {
//            return propiedad.get().getValor();
//        } else throw new RuntimeException("Clave no encontrada");
//    }

    private  String cifrar(String valorClaro) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] cifrado = cipher.doFinal(valorClaro.getBytes());
            return Base64.getEncoder().encodeToString(cifrado);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar la cookie", e);
        }
    }

    private  String descifrar(String valorCifrado) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] descifrado = cipher.doFinal(Base64.getDecoder().decode(valorCifrado));
            return new String(descifrado);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar la cookie", e);
        }
    }





    /**
     * Valida el formato de una cookie cifrada según las reglas de negocio.
     * Intenta descifrarla primero y luego valida el contenido en texto claro.
     * @param cookieValueCifrado Valor cifrado de la cookie
     * @return true si el valor descifrado cumple con el formato, false en caso contrario o si falla el descifrado
     */
    public  boolean validar(String cookieValueCifrado) {
        if (cookieValueCifrado == null || cookieValueCifrado.isEmpty()) return false;

        try {
            String valorClaro = descifrar(cookieValueCifrado);

            // Expresión regular que verifica:
            // - No permite dobles separadores (// o !!)
            // - No permite terminaciones con !
            // - Formato usuario!numero repetido
            String regex = "^(?!.*//)(?!.*!!)(?!.*!$)(?!^!)([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}!\\d+)(/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}!\\d+)*$";
            return valorClaro.matches(regex);

        } catch (Exception e) {
            // Falla de descifrado o cualquier excepción es considerada inválida
            return false;
        }
    }

/**
 * Convierte el valor de la cookie en un mapa de usuarios con sus contadores
 * @param cookieValue Valor de la cookie a deserializar
 * @return Mapa de <Usuario, Contador> o mapa vacío si es inválido
 */
public  Map<String, Integer> deserializar(String cookieValue) {
    if (!validar(cookieValue)) return new HashMap<>();
    cookieValue= descifrar(cookieValue);
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
public  String serializar(Map<String, Integer> usuarios) {
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
 * @param usuarios      Mapa actual de usuarios
 * @param valor         Valor actual de la cookie
 * @param usuarioActual Usuario a actualizar
 * @return Nuevo valor serializado para la cookie
 */
    public String actualizar(Map<String, Integer> usuarios, String valor, String usuarioActual) {
        int contador = usuarios.getOrDefault(usuarioActual, 0);
        usuarios.put(usuarioActual, ++contador);
        return serializar(usuarios);
    }

/**
 * Crea una nueva cookie con configuración de seguridad básica
 */
public void crearCookie(HttpServletResponse response, String nombre, String valor, int duracionSegundos) {
    String valorCifrado = cifrar(valor);
    Cookie cookie = new Cookie(nombre, valorCifrado);
    cookie.setPath("/");
    cookie.setMaxAge(duracionSegundos);
    response.addCookie(cookie);
}

/**
 * Elimina una cookie del cliente
 *
 * @param response Objeto HttpServletResponse
 * @param nombre   Nombre de la cookie a eliminar
 */
public  void eliminarCookie(HttpServletResponse response, String nombre) {
    Cookie cookie = new Cookie(nombre, "");
    cookie.setPath("/");
    cookie.setMaxAge(0); // Tiempo de vida cero para eliminación
    response.addCookie(cookie);
}

/**
 * Obtiene el valor de una cookie específica
 *
 * @param request Objeto HttpServletRequest
 * @param nombre  Nombre de la cookie a buscar
 * @return Valor de la cookie o null si no existe
 */
public  String obtenerValorCookie(HttpServletRequest request, String nombre) {
    if (request.getCookies() == null) return null;

    /*for (Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals(nombre)) {
            try {
                return descifrar(cookie.getValue());
            } catch (Exception e) {
                return null; // Si no puede descifrar, devuelve null por seguridad
            }
        }
    }
    return null;*/

    return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(nombre))
            .findFirst()
            .map(cookie -> {
                try {
                    return descifrar(cookie.getValue());
                } catch (Exception e) {
                    return null; // Si no puede descifrar, devuelve null por seguridad
                }
            })
            .orElse(null);
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

public int obtenerInicios(String cookie, String usuario){
    if (cookie == null || usuario == null) return 0;

    Map<String, Integer> usuarios = deserializar(cookie);
    return usuarios.getOrDefault(usuario, 1);
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
public void cerrarSesion(HttpServletResponse response, String nombre) {
    eliminarCookie(response, nombre);
}

}
