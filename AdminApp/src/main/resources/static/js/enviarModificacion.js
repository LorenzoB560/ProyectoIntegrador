var urlParams = new URLSearchParams(window.location.search);
var idEmpleado = window.location.pathname.split('/').pop() || urlParams.get('id');

$(document).ready(function() {
    $("#btn-Enviar").on("click", function() {
        const dto = {
            id: idEmpleado,
            nombre: $("#nombreEmpleado").val(),
            apellido: $("#apellidoEmpleado").val(),
            fechaNacimiento: $("#fechaNacimiento").val(),
            correo: $("#email").val()
        };

    $.ajax({
            type: "PUT",
            url: `/empleados/guardar-modificado/${idEmpleado}`,
            data: JSON.stringify(dto),
            contentType: "application/json",
            success() {
                console.log("Se ha modificado correctamente al usuario.")
                //Redireccion
                window.location.href = "/empleado/lista";
            }, error: function (xhr) {
                console.log(xhr.responseText)
            }
        });
    })
})