document.addEventListener("DOMContentLoaded", function () {
    const langSelect = document.getElementById('seleccionarIdioma');

    if (langSelect) {
        langSelect.addEventListener('change', function () {
            const selectedLang = this.value;
            const currentPath = window.location.pathname;
            const currentSearch = new URLSearchParams(window.location.search);

            currentSearch.set('lang', selectedLang);

            fetch(currentPath + '?' + currentSearch.toString(), {
                method: 'GET',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            }).then(() => {
                location.reload(); // recarga para aplicar idioma
            });
        });
    }
});
