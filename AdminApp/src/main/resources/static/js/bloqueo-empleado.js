// Espera a que el contenido del DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', () => {

    // Obtener referencias a los elementos del DOM
    const form = document.getElementById('formBloquear');
    const submitButton = document.getElementById('btnAsignarBloqueo');
    const errorDiv = document.getElementById('errorMensaje');
    const employeeNameElement = document.getElementById('employeeName');

    // --- Validar si los elementos existen antes de continuar ---
    if (!form || !submitButton || !errorDiv || !employeeNameElement) {
        console.error('Error: No se encontraron todos los elementos necesarios en el DOM (form, button, errorDiv, employeeName).');
        return;
    }

    // --- Obtener datos pasados desde HTML usando data-* attributes ---
    const employeeName = employeeNameElement.dataset.name || 'este empleado';
    const redirectUrl = form.dataset.redirectUrl; // URL para redirigir en caso de éxito
    const endpointUrl = form.action; // URL del endpoint POST para bloquear
    const csrfToken = form.dataset.csrfToken; // Token CSRF (si existe)
    const csrfHeader = form.dataset.csrfHeader; // Nombre de la cabecera CSRF (si existe)

    // --- Validar si las URLs necesarias están presentes ---
    if (!redirectUrl) {
        console.error('Error: Falta el atributo data-redirect-url en el formulario.');
        errorDiv.textContent = 'Error de configuración: Falta la URL de redirección.';
        errorDiv.style.display = 'block';
        return;
    }
    if (!endpointUrl) {
        console.error('Error: Falta el atributo action en el formulario.');
        errorDiv.textContent = 'Error de configuración: Falta la URL del endpoint.';
        errorDiv.style.display = 'block';
        return;
    }


    // --- Listener para el botón 'Asignar Bloqueo' ---
    submitButton.addEventListener('click', function(event) {
        // Aunque el botón no es type="submit", prevenir por si acaso
        event.preventDefault();
        errorDiv.style.display = 'none'; // Ocultar errores previos

        // 1. Validar que se ha seleccionado un motivo
        const selectedRadio = form.querySelector('input[name="motivoId"]:checked');
        if (!selectedRadio) {
            errorDiv.textContent = 'Por favor, selecciona un motivo de bloqueo.';
            errorDiv.style.display = 'block';
            return;
        }
        const motivoId = selectedRadio.value;

        // 2. Obtener texto del motivo para el mensaje (opcional)
        let reasonText = 'el motivo seleccionado';
        const label = form.querySelector(`label[for='${selectedRadio.id}']`);
        if (label) {
            reasonText = label.textContent.trim();
        }

        // 3. Diálogo de Confirmación
        if (confirm(`¿Estás seguro de bloquear a ${employeeName} por "${reasonText}"?`)) {

            // 4. Preparar y enviar petición Fetch
            // Deshabilitar botón mientras se procesa
            submitButton.disabled = true;
            submitButton.textContent = 'Asignando...';

            // --- Cabeceras ---
            const headers = {
                // Necesario para que Spring @RequestParam funcione con `application/x-www-form-urlencoded`
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            };
            // --- Añadir token CSRF si existe y es necesario ---
            if (csrfToken && csrfHeader && csrfToken !== 'null' && csrfHeader !== 'null') {
                headers[csrfHeader] = csrfToken;
            }

            // --- Cuerpo de la petición (para @RequestParam) ---
            const body = new URLSearchParams({
                'motivoId': motivoId
            });

            // --- Llamada Fetch ---
            fetch(endpointUrl, { // Usar la URL obtenida del form.action
                method: 'POST',
                headers: headers,
                body: body
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error(text || `Error del servidor: ${response.status}`);
                        });
                    }
                    return response;
                })
                .then(() => {
                    // 5. Redirección en caso de ÉXITO
                    console.log('Bloqueo asignado con éxito. Redirigiendo...');
                    window.location.href = redirectUrl + '?mensaje=bloqueo_exitoso'; // Usar la URL obtenida y añadir parámetro
                })
                .catch(error => {
                    // 6. Manejo de Errores
                    console.error('Error al asignar bloqueo:', error);
                    errorDiv.textContent = `Error al asignar bloqueo: ${error.message}`;
                    errorDiv.style.display = 'block';
                    // Reactivar el botón en caso de error
                    submitButton.disabled = false;
                    submitButton.textContent = 'Asignar Bloqueo';
                });

        } else {
            // Usuario canceló la confirmación
            console.log('Asignación de bloqueo cancelada.');
        }
    });

}); // Fin del listener DOMContentLoaded