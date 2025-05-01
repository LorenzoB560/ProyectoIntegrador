// Función que inicializa el control de CSS
function inicializaFuncion() {
    // Obtenemos los botones de radio
    const botonesRadio = document.querySelectorAll('input[name="modificarCSS"]');

    // Obtenemos la etiqueta style que contiene los estilos CSS
    const hojaEstilo = document.getElementById('estilos');

    // Añadimos evento para controlar los cambios en los botones de radio
    botonesRadio.forEach(button => {
        button.addEventListener('change', function() {
            modificaCSS(this.value, hojaEstilo);
        });
    });
}

function modificaCSS(valor, estilo) {
    if (valor === 'on') {
        // Activar CSS
        estilo.disabled = false;
    } else if (valor === 'off') {
        // Desactivar CSS
        estilo.disabled = true;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    inicializaFuncion();

    // Deshabilitamos el CSS por defecto al cargar la página
    const hojaEstilo = document.getElementById('estilos');
    hojaEstilo.disabled = true;
});