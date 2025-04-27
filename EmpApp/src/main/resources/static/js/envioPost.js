$(document).ready(function() {
    $("#btnEnviar").click(function() {
        if (confirm('¿Estás seguro que quieres crear el usuario?')) {
            // Limpiar mensajes previos
            $("#mensajes").empty();

            // Guardo los datos del formulario en variables.
            var datos = {
                usuario: $("#usuario").val(),
                clave: $("#clave").val(),
                confirmarClave: $("#confirmarClave").val()
            };

            // Esta es la petición AJAX para POST
            $.ajax({
                type: "POST",
                url: "/guardar-registro-usuario",
                data: JSON.stringify(datos),
                contentType: "application/json",
                success: function(response) {
                    // Mostrar mensaje de éxito
                    $("#mensajes").html("<div class='success'>El usuario " + $("#usuario").val() + " se ha creado correctamente</div>");
                },
                error: function(xhr) {
                    try {
                        var errorResponse = JSON.parse(xhr.responseText);
                        var mensajesHTML = "";

                        // Estructura específica de InformacionExcepcion
                        if (errorResponse.listaErrores && errorResponse.listaErrores.length > 0) {
                            $.each(errorResponse.listaErrores, function(i, mensaje) {
                                mensajesHTML += "<div class='error'>" + mensaje + "</div>";
                            });
                            $("#mensajes").html(mensajesHTML);
                        } else {
                            $("#mensajes").html("<div class='error'>Error al procesar el formulario</div>");
                        }
                    } catch (e) {
                        $("#mensajes").html("<div class='error'>Error al procesar el formulario</div>");
                    }
                }
            });
        }
    });
});
