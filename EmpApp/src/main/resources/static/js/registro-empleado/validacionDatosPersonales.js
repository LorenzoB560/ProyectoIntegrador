document.addEventListener('DOMContentLoaded', () => {
    // Definir los campos y sus tipos correspondientes
    const tiposCampo = {
        fechaNacimiento: { servidor: 'text', cliente: 'date' },
        edad: { servidor: 'text', cliente: 'number' },
    };

    // Función para cambiar los tipos de campo según la selección
    function actualizarTipoCampo(tipoValidacion) {
        for (const idCampo in tiposCampo) {
            const campo = document.getElementById(idCampo);
            if (campo) {
                // Guardar el valor actual
                const valorActual = campo.value;

                // Cambiar el tipo del campo
                campo.type = tiposCampo[idCampo][tipoValidacion];
                //Asignar el valor actual
                campo.value = valorActual;
            }
        }

        // Guardar la preferencia en localStorage
        localStorage.setItem('tipoValidacionPreferido', tipoValidacion);
    }

    // Escuchar cambios en los botones radio
    const radioButtons = document.querySelectorAll('input[name="tipoValidacion"]');
    if (radioButtons && radioButtons.length > 0) {
        radioButtons.forEach(radio => {
            radio.addEventListener('change', function() {
                actualizarTipoCampo(this.value);
            });
        });

        // Recuperar la preferencia guardada desde localStorage
        const tipoValidacionGuardado = localStorage.getItem('tipoValidacionPreferido');

        if (tipoValidacionGuardado) {
            // Seleccionar el botón correspondiente a la preferencia guardada
            const radioSeleccionado = document.querySelector(`input[name="tipoValidacion"][value="${tipoValidacionGuardado}"]`);
            if (radioSeleccionado) {
                radioSeleccionado.checked = true;
                actualizarTipoCampo(tipoValidacionGuardado);
            }
        } else {
            // Si no hay preferencia guardada, determinar qué botón está seleccionado al cargar la página
            const radioSeleccionado = document.querySelector('input[name="tipoValidacion"]:checked');
            if (radioSeleccionado) {
                actualizarTipoCampo(radioSeleccionado.value);
            } else {
                // Si ninguno está seleccionado, usar el tipo de validación de cliente por defecto
                const servidorRadio = document.querySelector('input[name="tipoValidacion"][value="servidor"]');
                if (servidorRadio) {
                    servidorRadio.checked = true;
                }
                actualizarTipoCampo('servidor');
            }
        }
    }

    // Añadir información del estado actual al formulario para guardar en el servidor
    const formulario = document.getElementById('formulario');
    if (formulario) {
        formulario.addEventListener('submit', function(event) {
            // Crear un campo oculto para guardar la preferencia de validación
            const tipoValidacionSeleccionado = document.querySelector('input[name="tipoValidacion"]:checked')?.value || 'servidor';

            // Verificar si ya existe el campo oculto
            let inputOculto = document.getElementById('tipoValidacionPreferido');
            if (!inputOculto) {
                inputOculto = document.createElement('input');
                inputOculto.type = 'hidden';
                inputOculto.id = 'tipoValidacionPreferido';
                inputOculto.name = 'tipoValidacionPreferido';
                formulario.appendChild(inputOculto);
            }

            inputOculto.value = tipoValidacionSeleccionado;
        });
    }
});