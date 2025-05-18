document.addEventListener('DOMContentLoaded', () => {
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

                // Si cambiamos de date a text, formateamos la fecha
                if (tiposCampo[idCampo][tipoValidacion] === 'text' &&
                    tiposCampo[idCampo]['cliente'] === 'date' &&
                    valorActual) {
                    try {
                        const date = new Date(valorActual);
                        if (!isNaN(date.getTime())) {
                            const day = String(date.getDate()).padStart(2, '0');
                            const month = String(date.getMonth() + 1).padStart(2, '0');
                            const year = date.getFullYear();
                            campo.value = `${day}/${month}/${year}`;
                        } else {
                            campo.value = valorActual;
                        }
                    } catch (e) {
                        campo.value = valorActual;
                    }
                } else {
                    // Para otros tipos, mantener el valor tal cual
                    campo.value = valorActual;
                }
            }
        }
    }

    // Escuchar cambios en los botones radio
    const radioButtons = document.querySelectorAll('input[name="tipoValidacion"]');
    if (radioButtons && radioButtons.length > 0) {
        radioButtons.forEach(radio => {
            radio.addEventListener('change', function() {
                actualizarTipoCampo(this.value);
            });
        });

        // Determinar qué botón está seleccionado al cargar la página
        const radioSeleccionado = document.querySelector('input[name="tipoValidacion"]:checked');
        if (radioSeleccionado) {
            actualizarTipoCampo(radioSeleccionado.value);
        } else {
            // Si ninguno está seleccionado, usar el tipo de validación de servidor por defecto
            actualizarTipoCampo('servidor');
        }
    } else {
        console.error('No se encontraron botones de radio para el tipo de validación');
    }

});