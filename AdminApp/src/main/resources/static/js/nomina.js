$(document).ready(function () {
    $('.btn-eliminar').on('click', function () {
        const idNomina = $(this).data('id-nomina');
        const idConcepto = $(this).data('id-concepto');
        const $listItem = $(this).closest('li');

        $.ajax({
            url: `/nomina/eliminar-concepto/${idNomina}/${idConcepto}`,
            method: 'DELETE',
            success: function () {
                alert("Concepto eliminado correctamente.");
                $listItem.remove();
                location.reload();  // Actualiza la vista con los nuevos datos
            },
            error: function (xhr) {
                alert("Error al eliminar el concepto.");
                console.error(xhr);
            }
        });
    });
});
