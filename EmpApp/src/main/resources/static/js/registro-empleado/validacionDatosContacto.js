document.addEventListener('DOMContentLoaded', () => {
    // Aquí preparo todo cuando la página está lista.

    // Elementos que usaré en varias partes.
    const formulario = document.getElementById('formulario');
    const radioButtonsTipoValidacion = document.querySelectorAll('input[name="tipoValidacion"]');

    // Helper para crear los divs donde mostraré los mensajes de error.
    function crearDivMensajeError(inputRef) {
        if (!inputRef || !inputRef.parentNode) return null;
        const div = document.createElement('div');
        div.className = 'error-validation-message'; // Una clase para darles estilo si quiero.
        div.style.cssText = 'color:red; font-size:0.9em; margin-top:5px; display:none;'; // Estilos básicos.
        inputRef.parentNode.insertBefore(div, inputRef.nextSibling);
        return div;
    }

    // --- VALIDACIÓN DEL DOCUMENTO DE IDENTIDAD ---
    const tipoDocumentoRadios = document.querySelectorAll('input[name="tipoDocumento"]');
    const numDocumentoInput = document.getElementById('numDocumento');
    const mensajeErrorDocumento = crearDivMensajeError(numDocumentoInput);

    // Mis reglas para cada tipo de documento.
    const validacionesDocumento = {
        DNI: { patron: /^[0-9]{8}[A-Z]$/, mensaje: 'DNI: 8 números y 1 letra mayúscula (Ej: 12345678Z).' },
        NIE: { patron: /^[XYZ][0-9]{7}[A-Z]$/, mensaje: 'NIE: X,Y o Z, 7 números y 1 letra (Ej: X1234567L).' },
        PASAPORTE: { patron: /^[A-Z0-9]{5,15}$/, mensaje: 'Pasaporte: 5-15 caracteres alfanuméricos.' }
    };

    // Esta función revisa si el documento es válido.
    function validarDocumento() {
        if (!numDocumentoInput || !mensajeErrorDocumento) return true; // Si no están los campos, no valido.

        const tipoSeleccionado = document.querySelector('input[name="tipoDocumento"]:checked');
        const tipo = tipoSeleccionado ? tipoSeleccionado.value.trim().toUpperCase() : '';
        const numero = numDocumentoInput.value.trim();

        // Limpio errores previos.
        mensajeErrorDocumento.style.display = 'none';
        mensajeErrorDocumento.textContent = '';
        numDocumentoInput.setCustomValidity('');

        if (!tipo || !numero || !validacionesDocumento[tipo]) return true; // Sin tipo, número o regla, no hay error aquí.

        const { patron, mensaje } = validacionesDocumento[tipo];
        if (!patron.test(numero)) {
            mensajeErrorDocumento.textContent = mensaje;
            mensajeErrorDocumento.style.display = 'block';
            numDocumentoInput.setCustomValidity(mensaje); // Para que el navegador también sepa del error.
            return false;
        }
        return true;
    }

    // Activo o desactivo los listeners para la validación del documento.
    function gestionarListenersValidacionDocumento(activar) {
        if (!numDocumentoInput || !tipoDocumentoRadios || !tipoDocumentoRadios.length) return;
        const eventos = ['input', 'blur'];
        tipoDocumentoRadios.forEach(radio => {
            radio[activar ? 'addEventListener' : 'removeEventListener']('change', validarDocumento);
        });
        eventos.forEach(evento => {
            numDocumentoInput[activar ? 'addEventListener' : 'removeEventListener'](evento, validarDocumento);
        });

        if (activar) validarDocumento(); // Compruebo el estado actual al activar.
        else if (mensajeErrorDocumento) { // Limpio si desactivo.
            mensajeErrorDocumento.style.display = 'none';
            mensajeErrorDocumento.textContent = '';
            numDocumentoInput.setCustomValidity('');
        }
    }

    // --- VALIDACIÓN DEL NÚMERO DE TELÉFONO ---
    const numTelefonoInput = document.getElementById('numTelefono');
    const mensajeErrorTelefono = crearDivMensajeError(numTelefonoInput);
    const patronTelefono = /^\d{9}$/; // Espero 9 dígitos exactos.
    const mensajeErrorTextoTelefono = 'Teléfono: Debe tener 9 dígitos.';

    // Reviso si el teléfono es válido.
    function validarTelefono() {
        if (!numTelefonoInput || !mensajeErrorTelefono) return true;

        const valor = numTelefonoInput.value.trim();
        mensajeErrorTelefono.style.display = 'none';
        mensajeErrorTelefono.textContent = '';
        numTelefonoInput.setCustomValidity('');

        if (valor === "") return true; // Vacío no es error para este script.

        if (!patronTelefono.test(valor)) {
            mensajeErrorTelefono.textContent = mensajeErrorTextoTelefono;
            mensajeErrorTelefono.style.display = 'block';
            numTelefonoInput.setCustomValidity(mensajeErrorTextoTelefono);
            return false;
        }
        return true;
    }

    // Activo o desactivo los listeners para la validación del teléfono.
    function gestionarListenersValidacionTelefono(activar) {
        if (!numTelefonoInput) return;
        const eventos = ['input', 'blur'];
        eventos.forEach(evento => {
            numTelefonoInput[activar ? 'addEventListener' : 'removeEventListener'](evento, validarTelefono);
        });

        if (activar) validarTelefono();
        else if (mensajeErrorTelefono) {
            mensajeErrorTelefono.style.display = 'none';
            mensajeErrorTelefono.textContent = '';
            numTelefonoInput.setCustomValidity('');
        }
    }

    // --- GESTIÓN DEL MODO DE VALIDACIÓN (CLIENTE/SERVIDOR) ---
    const KEY_STORAGE_VALIDACION = 'tipoValidacionPreferido_DatosContacto'; // Clave para guardar mi elección.

    // Esta función aplica el modo de validación elegido.
    function aplicarModoValidacion(modo) {
        const esCliente = (modo === 'cliente');
        gestionarListenersValidacionDocumento(esCliente);
        gestionarListenersValidacionTelefono(esCliente);
    }

    // Configuro los botones de radio para cambiar el modo de validación.
    if (radioButtonsTipoValidacion && radioButtonsTipoValidacion.length > 0) {
        radioButtonsTipoValidacion.forEach(radio => {
            radio.addEventListener('change', function() {
                localStorage.setItem(KEY_STORAGE_VALIDACION, this.value); // Guardo mi elección.
                aplicarModoValidacion(this.value);
            });
        });

        // Al cargar, veo si tenía una preferencia guardada, o uso la que esté en el HTML.
        let modoInicial = localStorage.getItem(KEY_STORAGE_VALIDACION);
        const radioSeleccionadoHTML = document.querySelector('input[name="tipoValidacion"]:checked');

        if (modoInicial) {
            const radioGuardado = document.querySelector(`input[name="tipoValidacion"][value="${modoInicial}"]`);
            if (radioGuardado) radioGuardado.checked = true;
            else modoInicial = radioSeleccionadoHTML?.value || 'servidor'; // Si lo guardado es raro, uso el HTML.
        } else {
            modoInicial = radioSeleccionadoHTML?.value || 'servidor';
        }
        localStorage.setItem(KEY_STORAGE_VALIDACION, modoInicial); // Aseguro que esté guardado.
        if(radioSeleccionadoHTML && radioSeleccionadoHTML.value !== modoInicial) { // Corrijo el HTML si no coincide
            const radioAChequear = document.querySelector(`input[name="tipoValidacion"][value="${modoInicial}"]`);
            if(radioAChequear) radioAChequear.checked = true;
        }
        aplicarModoValidacion(modoInicial); // Aplico el modo.
    } else {
        aplicarModoValidacion('servidor'); // Si no hay radios, modo servidor por defecto.
    }

    // --- MANEJO DEL ENVÍO DEL FORMULARIO ---
    if (formulario) {
        formulario.addEventListener('submit', function(event) {
            const modoActivo = document.querySelector('input[name="tipoValidacion"]:checked')?.value || 'servidor';

            // Añado un campo oculto para que el servidor sepa mi preferencia.
            let campoOculto = formulario.querySelector('input[name="tipoValidacionPreferido"]');
            if (!campoOculto) {
                campoOculto = document.createElement('input');
                campoOculto.type = 'hidden';
                campoOculto.name = 'tipoValidacionPreferido';
                formulario.appendChild(campoOculto);
            }
            campoOculto.value = modoActivo;

            // Si estoy en modo cliente, valido todo antes de enviar.
            if (modoActivo === 'cliente') {
                // Ejecuto ambas validaciones y si alguna falla, detengo el envío.
                // El `&&` asegura que validarTelefono() solo se llame si validarDocumento() es true,
                // pero quiero que ambas se ejecuten para mostrar todos los errores.
                const docValido = validarDocumento();
                const telValido = validarTelefono();

                if (!docValido || !telValido) {
                    event.preventDefault(); // ¡Alto ahí! Hay errores.
                    alert('Por favor, corrige los errores resaltados antes de enviar.');
                }
            }
        });
    }
});