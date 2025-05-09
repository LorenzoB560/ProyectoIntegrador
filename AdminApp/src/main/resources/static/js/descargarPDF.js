$(document).ready(function () {
    $(document).on('click', '#descargar-pdf-btn', function () {
        const idNomina = $(this).data('id');

        $.ajax({
            url: '/nomina/descargar-pdf/' + idNomina,
            method: 'GET',
            xhrFields: {
                responseType: 'blob'
            },
            success: function (data) {
                const blob = new Blob([data], { type: 'application/pdf' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'nomina_' + idNomina + '.pdf';
                document.body.appendChild(a);
                a.click();
                a.remove();
            },
            error: function () {
                alert('Error al generar la nómina en PDF.');
            }
        });
    });
});
