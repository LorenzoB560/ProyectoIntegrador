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

function cambiarPeticion() {
    // Eliminar el select y mostrar el input
    const select = document.getElementById('usuariosCookie');
    const input = document.createElement('input');
    input.type = 'text';
    input.name = 'correo';
    input.placeholder = 'Correo electrónico';
    input.required = true;

    // Reemplazar el select por el nuevo input
    select.parentNode.replaceChild(input, select);
}

function alternarEntradaUsuario() {
    let selectContainer = document.getElementById("selectContainer");
    let inputContainer = document.getElementById("inputContainer");
    let usuariosSelect = document.getElementById("usuariosSelect");
    let usuarioInput = document.getElementById("usuarioInput");
    let form = document.getElementById("usuarioForm");

    if (selectContainer.hidden) {
        // Volver al select (autenticados)
        selectContainer.hidden = false;
        inputContainer.hidden = true;
        usuariosSelect.removeAttribute("disabled");
        usuarioInput.setAttribute("disabled", "true");

        // Restablecer action al endpoint de validar clave
        form.action = "/validar-clave";
    } else {
        // Pasar a input manual
        selectContainer.hidden = true;
        inputContainer.hidden = false;
        usuariosSelect.setAttribute("disabled", "true");
        usuarioInput.removeAttribute("disabled");

        // Cambiar action al endpoint de validar usuario
        form.action = "/validar-usuario";
    }
}


function actualizarAction(esNuevoUsuario) {
    let form = document.getElementById("usuarioForm");
    if (esNuevoUsuario) {
        form.action = "/validar-usuario";
    } else {
        form.action = "/validar-clave";
    }
}

$(document).ready(function() {
    // Se ejecuta cuando el DOM está completamente cargado

    $("#recordarContraseña").click(function(event) {
        // Captura el clic sobre el botón o enlace "¿Recordar contraseña?"

        event.preventDefault();
        // Evita que el enlace haga su comportamiento por defecto (redirigir o recargar)

        const correo = $("#correo").text().trim();
        // Obtiene el valor del input con ID "correo", eliminando espacios en blanco

        if (correo) {
            // Si se ha introducido un correo...

            $.ajax({
                url: "/empapp/devuelve-clave", // Endpoint que expone la contraseña
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