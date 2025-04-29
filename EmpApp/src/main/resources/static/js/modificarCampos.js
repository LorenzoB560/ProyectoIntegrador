function seleccionarPrimerGenero() {
    const radios = document.querySelectorAll('input[name="idGeneroSeleccionado"]');
    if (radios.length > 0) {
        radios[0].checked = true;
    }
}

function deseleccionaRadios(botonesRadioName) {
    const elementos = document.getElementsByName(botonesRadioName);
    for (var i = 0; i < elementos.length; i++) {
        if (elementos[i].type === "radio") {
            elementos[i].checked = false;
        }
    }
}
function deseleccionaSelect(botonesSelectName) {
    const elementos = document.getElementsByName(botonesSelectName);
    for (var i = 0; i < elementos.length; i++) {
        if (elementos[i].type === "select-one" || elementos[i].type === "select-multiple") {
            const opciones = elementos[i].options;
            for (var j = 0; j < opciones.length; j++) {
                opciones[j].selected = false;
            }
        }
    }
}
function seleccionaSelect(botonesSelectName) {
    const elementos = document.getElementsByName(botonesSelectName);
    for (var i = 0; i < elementos.length; i++) {
        if (elementos[i].type === "select-one" || elementos[i].type === "select-multiple") {
            const opciones = elementos[i].options;
            for (var j = 0; j < opciones.length; j++) {
                opciones[j].selected = true;
            }
        }
    }
}
function deseleccionaCheckbox(botonesCheckboxName) {
    const elementos = document.getElementsByName(botonesCheckboxName);
    for (var i = 0; i < elementos.length; i++) {
        if (elementos[i].type === "checkbox") {
            elementos[i].checked = false;
        }
    }
}
function seleccionaCheckbox(botonesCheckboxName) {
    const elementos = document.getElementsByName(botonesCheckboxName);
    for (var i = 0; i < elementos.length; i++) {
        if (elementos[i].type === "checkbox") {
            elementos[i].checked = true;
        }
    }
}

function vaciarCampos() {
    var formulario = document.getElementById('formulario');

    // Limpiar campos de texto
    var inputs = formulario.querySelectorAll('input[type="text"], input[type="password"]');
    inputs.forEach(function(input) {
        input.value = '';
    });

    // Deseleccionar checkboxes
    var checkboxes = formulario.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = false;
    });

    // Deseleccionar radio buttons
    var radios = formulario.querySelectorAll('input[type="radio"]');
    radios.forEach(function(radio) {
        radio.checked = false;
    });

    // Vaciar selects (dejar la opción por defecto)
    var selects = formulario.querySelectorAll('select');
    selects.forEach(function(select) {
        select.selectedIndex = -1; // Deselecciona todas las opciones
    });

    // Limpiar campos de texto grandes (textarea)
    var textareas = formulario.querySelectorAll('textarea');
    textareas.forEach(function(textarea) {
        textarea.value = '';
    });

}