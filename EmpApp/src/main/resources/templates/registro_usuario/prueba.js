$(document).ready(function(){
    $("#btn-Enviar").on("click", function(){
        var datos = {
            usuario: $("#usuario").val(),
            clave: $("#clave").val(),
            confirmarClave: $("confirmarClave").val(),
        }

        $.ajax({
            type: "POST",
            url: "/guardar-datos-usuario",
            data: JSON.stringify(datos),
            contentType: "application/json; charset=utf-8",
            success(){
                console.log("Success")
            }, error: function(xhr, status, error) {
                console.log(xhr.responseText);
            }
        })
    })
})