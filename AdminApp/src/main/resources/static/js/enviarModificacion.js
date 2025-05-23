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
                console.log("Se ha modificado el empleado correctamente")
                window.location.href = "/adminapp/empleado/lista";
            }, error: function (xhr) {
                console.log(xhr.responseText)
            }
        });
    })
})