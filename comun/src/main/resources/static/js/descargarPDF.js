$(document).ready(function () {

    // Se asigna un evento al botón con id "descargar-pdf-btn"
    $(document).on('click', '#descargar-pdf-btn', function () {

        // Se obtiene el ID de la nómina desde el atributo "data-id" del botón clicado
        const idNomina = $(this).data('id');

        // Se realiza una solicitud AJAX para descargar el PDF de la nómina
        $.ajax({
            url: '/nomina/descargar-pdf/' + idNomina,
            method: 'GET',
            xhrFields: {
                responseType: 'blob' // Indica que la respuesta será un archivo binario (PDF)
            },
            success: function (data) {
                // Se crea un objeto Blob con los datos recibidos, indicando que es un archivo PDF
                const blob = new Blob([data], {type: 'application/pdf'});

                // Se genera una URL temporal que apunta al archivo PDF generado
                const url = window.URL.createObjectURL(blob);

                // Se crea un elemento <a> para facilitar la descarga automática
                const a = document.createElement('a');
                a.href = url;
                a.download = 'nomina_' + idNomina + '.pdf'; // Se define el nombre del archivo a descargar

                // Se agrega temporalmente el elemento <a> al documento y se simula un clic para iniciar la descarga
                document.body.appendChild(a);
                a.click();

                // Se elimina el elemento <a> después de la descarga para limpiar el DOM
                a.remove();
            },
            error: function () {
                alert('Error al generar la nómina en PDF.');
            }
        });
    });
});
