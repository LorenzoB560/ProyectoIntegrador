document.addEventListener("DOMContentLoaded", function () {

    // Se obtiene el elemento del selector de idioma por su ID
    const langSelect = document.getElementById('seleccionarIdioma');

    if (langSelect) {

        // Se agrega un evento de cambio (change) al selector de idioma
        langSelect.addEventListener('change', function () {
            // Se obtiene el valor del idioma seleccionado por el usuario
            const selectedLang = this.value;

            // Se obtiene la URL actual de la página sin parámetros
            const currentPath = window.location.pathname;

            // Se obtiene los parámetros actuales de la URL y los convierte en un objeto manipulable
            const currentSearch = new URLSearchParams(window.location.search);

            // Se añade o actualiza el parámetro "lang" con el idioma seleccionado
            currentSearch.set('lang', selectedLang);

            // Se realiza una solicitud GET al servidor con la nueva configuración de idioma
            fetch(currentPath + '?' + currentSearch.toString(), {
                method: 'GET',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest' // Indica que la solicitud es AJAX
                }
            }).then(() => {
                // Recarga la página para aplicar el nuevo idioma seleccionado
                location.reload();
            });
        });
    }
});
