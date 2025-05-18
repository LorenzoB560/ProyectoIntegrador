// Este código debe ir en el archivo /js/registro-empleado/validacionDocumentos.js

document.addEventListener('DOMContentLoaded', () => {
    // Obtener referencias a los campos
    const tipoDocumentoSelect = document.getElementById('tipoDocumento');
    const numDocumentoInput = document.getElementById('numDocumento');

    if (!tipoDocumentoSelect || !numDocumentoInput) {
        console.log('Campos de documento no encontrados en esta página');
        return;
    }

    // Configuración de validación para distintos tipos de documentos
    const validacionesDocumento = {
        DNI: {
            patron: /^[0-9]{8}[A-Z]$/,
            mensaje: 'El DNI debe tener 8 números seguidos de una letra mayúscula (Ej: 12345678Z)'
        },
        NIE: {
            patron: /^[XYZ][0-9]{7}[A-Z]$/,
            mensaje: 'El NIE debe comenzar con X, Y o Z, seguido de 7 números y una letra mayúscula (Ej: X1234567L)'
        },
        PASAPORTE: {
            patron: /^[A-Z0-9]{5,15}$/,
            mensaje: 'El pasaporte debe tener entre 5 y 15 caracteres alfanuméricos'
        }
    };

    // Crear elementos para mostrar mensajes de error
    const mensajeError = document.createElement('div');
    mensajeError.className = 'error';
    mensajeError.style.display = 'none';
    mensajeError.style.color = 'red';
    mensajeError.style.fontSize = '0.9em';
    mensajeError.style.marginTop = '5px';

    // Insertar el elemento de mensaje después del campo de número de documento
    numDocumentoInput.parentNode.insertBefore(mensajeError, numDocumentoInput.nextSibling);

    // Función para validar el documento
    function validarDocumento() {
        const tipo = tipoDocumentoSelect.value.trim().toUpperCase();
        const numero = numDocumentoInput.value.trim();

        // Si el tipo no está en nuestras validaciones o no hay número, no validamos
        if (!tipo || !numero || !validacionesDocumento[tipo]) {
            mensajeError.style.display = 'none';
            numDocumentoInput.setCustomValidity('');
            return true;
        }

        const validacion = validacionesDocumento[tipo];
        const esValido = validacion.patron.test(numero);

        if (!esValido) {
            mensajeError.textContent = validacion.mensaje;
            mensajeError.style.display = 'block';
            numDocumentoInput.setCustomValidity(validacion.mensaje);
        } else {
            mensajeError.style.display = 'none';
            numDocumentoInput.setCustomValidity('');
        }

        return esValido;
    }

    // Conectar con el sistema de validación cliente/servidor
    function actualizarValidacionDocumento(tipoValidacion) {
        if (tipoValidacion === 'cliente') {
            // Activar validación del lado del cliente
            numDocumentoInput.addEventListener('input', validarDocumento);
            numDocumentoInput.addEventListener('blur', validarDocumento);
            tipoDocumentoSelect.addEventListener('change', validarDocumento);

            // Validar estado actual
            validarDocumento();
        } else {
            // Desactivar validación del lado del cliente
            numDocumentoInput.removeEventListener('input', validarDocumento);
            numDocumentoInput.removeEventListener('blur', validarDocumento);
            tipoDocumentoSelect.removeEventListener('change', validarDocumento);

            // Limpiar mensajes de error
            mensajeError.style.display = 'none';
            numDocumentoInput.setCustomValidity('');
        }
    }

    // Escuchar cambios en los botones radio de tipo de validación
    const radioButtons = document.querySelectorAll('input[name="tipoValidacion"]');
    if (radioButtons && radioButtons.length > 0) {
        radioButtons.forEach(radio => {
            radio.addEventListener('change', function() {
                actualizarValidacionDocumento(this.value);
            });
        });

        // Aplicar la validación según el botón seleccionado al cargar
        const radioSeleccionado = document.querySelector('input[name="tipoValidacion"]:checked');
        if (radioSeleccionado) {
            actualizarValidacionDocumento(radioSeleccionado.value);
        } else {
            // Por defecto, usar validación cliente
            actualizarValidacionDocumento('servidor');
        }
    }

    // Integración con el envío del formulario
    const formulario = document.getElementById('formulario');
    if (formulario) {
        formulario.addEventListener('submit', function(event) {
            const tipoValidacion = document.querySelector('input[name="tipoValidacion"]:checked')?.value || 'servidor';

            // Si estamos en validación cliente, validar antes de enviar
            if (tipoValidacion === 'cliente') {
                const documentoValido = validarDocumento();
                if (!documentoValido) {
                    event.preventDefault();
                    alert('Por favor, corrige los errores en el formulario antes de enviar.');
                }
            }
        });
    }
});