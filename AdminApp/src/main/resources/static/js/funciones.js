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
    // Se ejecuta cuando el DOM está completamente cargado

    $("#recordarContraseña").click(function(event) {
        // Captura el clic sobre el botón o enlace "¿Recordar contraseña?"

        event.preventDefault();
        // Evita que el enlace haga su comportamiento por defecto (redirigir o recargar)

        var correo = $("#correo").val().trim();
        // Obtiene el valor del input con ID "correo", eliminando espacios en blanco

        if (correo) {
            // Si se ha introducido un correo...

            $.ajax({
                url: "/adminapp/devuelve-clave", // Endpoint que expone la contraseña
                type: "GET",                     // Método HTTP
                data: { correo: correo },        // Parámetro que se envía al backend
                success: function(clave) {
                    // Función que se ejecuta si la petición se completa correctamente

                    alert("La contraseña de " + correo + " es: " + clave);
                    // Muestra la contraseña recibida
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    // Función que se ejecuta si ocurre un error en la llamada AJAX
                    console.error("Error en la llamada AJAX:", textStatus, errorThrown);
                    alert("Error al obtener la contraseña.");
                }
            });

        } else {
            // Si el campo correo está vacío...
            alert("Por favor, introduce tu correo para poder recuperar la contraseña.");
        }
    });
});