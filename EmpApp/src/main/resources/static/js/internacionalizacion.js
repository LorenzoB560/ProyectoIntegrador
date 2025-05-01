function setLanguageSelect() {
    const urlParams = new URLSearchParams(window.location.search);
    const lang = urlParams.get('lang') || 'es';
    const langSelect = document.getElementById('seleccionarIdioma');
    langSelect.value = lang;

    // Añadir ?lang=xx a todos los <a> sin parámetro de idioma
    document.querySelectorAll('a').forEach(a => {
        const href = a.getAttribute('href');
        if (href && !href.includes('lang=')) {
            const separator = href.includes('?') ? '&' : '?';
            a.setAttribute('href', href + separator + 'lang=' + lang);
        }
    });
}

window.onload = setLanguageSelect;

document.getElementById('seleccionarIdioma').addEventListener('change', function () {
    const lang = this.value;
    const currentUrl = window.location.pathname;
    window.location.href = currentUrl + '?lang=' + lang;
});
