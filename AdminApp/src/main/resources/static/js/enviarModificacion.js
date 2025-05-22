var urlParams = new URLSearchParams(window.location.search);
var idEmpleado = window.location.pathname.split('/').pop() || urlParams.get('id');

$(document).ready(function() {
    $("#btn-Enviar").on("click", function() {
        const dto = {
            id: idEmpleado,
            nombre: $("#nombreEmpleado").val(),
            apellido: $("#apellidoEmpleado").val(),
            fechaNacimiento: $("#fechaNacimiento").val(),
        };

    $.ajax({
            type: "PUT",
            url: `/adminapp/empleados/guardar-modificado/${idEmpleado}`,
            data: JSON.stringify(dto),
            contentType: "application/json",
            success() {
                window.location.href = "/adminapp/empleado/lista";
            },
            error: function (jqXHR, textStatus, errorThrown) {
            console.error("Error al guardar la nómina:", errorThrown);

            try {
                const errorJson = JSON.parse(jqXHR.responseText);

                if (errorJson.listaErrores && Array.isArray(errorJson.listaErrores)) {
                    const mensajePersonalizado = "Se encontraron los siguientes errores:\n- " + errorJson.listaErrores.join("\n- ");
                    alert(mensajePersonalizado);
                } else if (errorJson.message) {
                    alert("Error al guardar la nómina: " + errorJson.message);
                }
            } catch (e) {
                alert("Error inesperado al guardar la nómina: " + errorThrown);
            }
        }
        });
    })
})