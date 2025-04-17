function mostrarOcultarClave() {
    // Obtener los elementos de los campos de contraseña
    var clave = document.getElementById("clave");

    // Cambiar el tipo de ambos campos
    if (clave.type === "password") {
        clave.type = "text";  // Cambiar a tipo 'text' para mostrar la clave
    } else {
        clave.type = "password";  // Cambiar a tipo 'password' para ocultar la clave
    }
}

$(document).ready(function() {
    // Cuando se hace clic en el enlace "¿Recordar contraseña?"
    $("#recordarContraseña").click(function(event) {
        event.preventDefault(); // Evita que el enlace redirija

        // Obtén el valor del <h2> que contiene el nombre del usuario
        var usuario = $("#nombreUsuario").text().trim();
        // alert(usuario);
        if (usuario) {
            // Realiza la llamada AJAX
            $.ajax({
                url: "/devuelve-clave",
                type: "GET",
                data: { usuario: usuario },
                success: function(contraseña) {
                    // Muestra la contraseña en un alert
                    alert("La contraseña de " + usuario + " es: " + contraseña);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error("Error en la llamada AJAX:", textStatus, errorThrown); // Depuración
                    alert("Error al obtener la contraseña.");
                }
            });
        } else {
            alert("Por favor, selecciona un usuario.");
        }
    });
});