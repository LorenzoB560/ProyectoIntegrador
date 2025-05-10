$(document).ready(function () {
    $('.btn-eliminar').on('click', function () {
        const idNomina = $(this).data('id-nomina');
        const $tr = $(this).closest('tr');

        $.ajax({
            url: `/nomina/eliminar-nomina/${idNomina}`,
            method: 'DELETE',
            success: function () {
                alert("Nómina eliminada correctamente correctamente.");
                $tr.remove();
                location.reload();  // Actualiza la vista con los nuevos datos
            },
            error: function (xhr) {
                // Comprueba si la respuesta tiene un mensaje de error específico
                const errorMessage = xhr.responseJSON && xhr.responseJSON.message
                    ? xhr.responseJSON.message
                    : "Error al eliminar la nómina.";  // Mensaje por defecto si no hay mensaje específico
                alert(errorMessage);
                console.error(xhr);
            }
        });
    });
});
