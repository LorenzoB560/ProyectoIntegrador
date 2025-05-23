// Mostrar u ocultar la contraseña
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


$(document).ready(function () {
    // Recuperar contraseña (solo para pruebas, en producción solo por email)
    $("#recordarContraseña").click(function (event) {
        event.preventDefault();
        const correo = $("#correo").val().trim();

        if (!correo) {
            alert("Por favor, introduce tu correo para poder recuperar la contraseña.");
            return;
        }

        $.ajax({
            // url: `${contextPath}/devuelve-clave`,
            url: "/adminapp/devuelve-clave",
            type: "GET",
            data: { correo: correo },
            success: function (clave) {
                // En producción, solo se debe enviar por email. Aquí solo para pruebas.
                alert("La contraseña de " + correo + " es: " + clave);
            },
            error: function () {
                alert("Error al obtener la contraseña.");
            }
        });
    });

    // Envío del formulario de login por AJAX
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        // Limpiar mensajes previos
        $("#errorCredenciales").hide();
        $("#errorMessage").html("");

        const correo = $("#correo").val().trim();
        const clave = $("#clave").val();

        // Preparar datos para el backend
        const loginData = {
            usuario: correo,
            clave: clave
        };

        $.ajax({
            type: "POST",
            url: "/adminapp/login",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: function () {
                // Login exitoso: redirigir al área personal
                window.location.href = "/adminapp/area-personal";
            },
            error: function (xhr) {
                let response = {};
                try { response = JSON.parse(xhr.responseText); } catch (e) {}

                // Mensaje de error por defecto
                let errorMessage = "Ocurrió un error al procesar la solicitud. Intenta nuevamente.";

                // Errores de validación o credenciales
                if (xhr.status === 400) {
                    if (response.listaErrores && response.listaErrores.length > 0) {
                        errorMessage = response.listaErrores.join("<br>");
                    } else if (response.message) {
                        errorMessage = response.message;
                    }
                }

                $("#errorCredenciales").show();
                $("#errorMessage").html(errorMessage);
            }
        });
    });
});
