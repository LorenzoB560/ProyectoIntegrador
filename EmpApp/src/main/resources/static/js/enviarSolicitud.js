document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('formEnviarSolicitud');
    const mensajeAlertaDiv = document.getElementById('mensajeAlerta');
    const idReceptorSelect = document.getElementById('idReceptor');
    const submitButton = form.querySelector('button[type="submit"]');

    if (!form || !mensajeAlertaDiv || !idReceptorSelect || !submitButton) {
        console.error('No se encontraron todos los elementos necesarios en el DOM para el script de enviar solicitud.');
        return;
    }

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Evitar el envío tradicional del formulario
        mensajeAlertaDiv.innerHTML = ''; // Limpiar mensajes previos
        submitButton.disabled = true; // Deshabilitar botón para evitar envíos múltiples
        submitButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Enviando...';


        const idReceptor = idReceptorSelect.value;

        if (!idReceptor) {
            mostrarMensaje('Por favor, selecciona un empleado.', 'danger');
            idReceptorSelect.classList.add('is-invalid');
            submitButton.disabled = false; // Rehabilitar botón
            submitButton.innerHTML = '<i class="fas fa-paper-plane me-2"></i>Enviar Solicitud';
            return;
        }
        idReceptorSelect.classList.remove('is-invalid');

        const formData = new FormData();
        formData.append('idReceptor', idReceptor);

        // La URL del RestController
        const apiUrl = '/api/empapp/colaboraciones/solicitar';

        fetch(apiUrl, {
            method: 'POST',
            body: new URLSearchParams(formData),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
            .then(async response => {
                const responseBody = await response.json(); // Siempre intentar parsear JSON
                if (!response.ok) {
                    // Usar el mensaje de error del backend si está disponible
                    const errorMsg = responseBody.error || `Error ${response.status}: ${response.statusText}`;
                    throw new Error(errorMsg);
                }
                return responseBody;
            })
            .then(data => {
                if (data.mensaje) {
                    mostrarMensaje(data.mensaje, 'success');
                    form.reset(); // Limpiar el formulario en caso de éxito
                    idReceptorSelect.focus();
                } else if (data.error) { // Manejo explícito de la propiedad 'error'
                    mostrarMensaje(data.error, 'danger');
                } else {
                    mostrarMensaje('Respuesta inesperada del servidor.', 'warning');
                }
            })
            .catch(error => {
                console.error('Error al enviar solicitud:', error);
                mostrarMensaje('Error al enviar la solicitud: ' + error.message, 'danger');
            })
            .finally(() => {
                submitButton.disabled = false; // Rehabilitar botón
                submitButton.innerHTML = '<i class="fas fa-paper-plane me-2"></i>Enviar Solicitud';
            });
    });

    function mostrarMensaje(mensaje, tipo) {
        // Asegurarse de que solo haya un mensaje a la vez
        mensajeAlertaDiv.innerHTML = '';
        const wrapper = document.createElement('div');
        wrapper.innerHTML = `<div class="alert alert-${tipo} alert-dismissible fade show" role="alert">
                                ${mensaje}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                             </div>`;
        mensajeAlertaDiv.append(wrapper);

        // Scroll hacia el mensaje para visibilidad
        mensajeAlertaDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
});