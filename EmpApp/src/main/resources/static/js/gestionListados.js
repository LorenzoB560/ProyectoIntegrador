document.addEventListener('DOMContentLoaded', function () {
    const mensajeAlertaGlobalDiv = document.getElementById('mensajeAlertaGlobal');

    // Función para mostrar mensajes globales en la página de listados
    function mostrarMensajeGlobal(mensaje, tipo) {
        if (!mensajeAlertaGlobalDiv) return;
        mensajeAlertaGlobalDiv.innerHTML =
            `<div class="alert alert-${tipo} alert-dismissible fade show" role="alert">
                ${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
             </div>`;
        mensajeAlertaGlobalDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }

    // Manejar acciones de Aceptar/Rechazar
    document.querySelectorAll('.btn-accion-solicitud').forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            const form = this.closest('form'); // Encuentra el formulario padre del botón
            if (!form) return;

            const idSolicitud = form.dataset.solicitudId;
            const accion = form.dataset.accion; // 'aceptar' o 'rechazar'
            const apiUrl = `/api/empapp/colaboraciones/solicitudes/${idSolicitud}/${accion}`;

            // Feedback visual en el botón
            const originalButtonHtml = this.innerHTML;
            this.disabled = true;
            this.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Procesando...`;

            fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                    // Si tuvieras CSRF (necesitarías enviarlo si es POST):
                    // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                }
                // No se necesita body para estas acciones si el ID va en la URL
            })
                .then(async response => {
                    const responseBody = await response.json().catch(() => ({})); // Intenta parsear JSON, si falla, objeto vacío
                    if (!response.ok) {
                        const errorMsg = responseBody.error || `Error ${response.status}: ${response.statusText}`;
                        throw new Error(errorMsg);
                    }
                    return responseBody;
                })
                .then(data => {
                    if (data.mensaje) {
                        mostrarMensajeGlobal(data.mensaje, 'success');
                        // Opcional: Actualizar la fila o recargar la lista dinámicamente
                        // Por simplicidad, podrías simplemente eliminar la fila de la solicitud procesada:
                        const filaParaEliminar = form.closest('tr');
                        if (filaParaEliminar) {
                            filaParaEliminar.style.opacity = '0';
                            setTimeout(() => {
                                filaParaEliminar.remove();
                                // Comprobar si la tabla de recibidas quedó vacía
                                const tablaRecibidasBody = document.getElementById('tablaSolicitudesRecibidasBody');
                                const mensajeVacioRecibidas = document.getElementById('mensajeVacioRecibidas');
                                if (tablaRecibidasBody && mensajeVacioRecibidas && tablaRecibidasBody.children.length === 0) {
                                    mensajeVacioRecibidas.style.display = 'block';
                                    document.getElementById('tablaSolicitudesRecibidas').style.display = 'none';
                                }
                            }, 300); // Esperar a que termine la transición de opacidad
                        }
                    } else if (data.error) {
                        mostrarMensajeGlobal(data.error, 'danger');
                    } else {
                        mostrarMensajeGlobal('Respuesta inesperada del servidor.', 'warning');
                    }
                })
                .catch(error => {
                    console.error(`Error al ${accion} la solicitud:`, error);
                    mostrarMensajeGlobal(`Error al ${accion} la solicitud: ${error.message}`, 'danger');
                })
                .finally(() => {
                    // Restaurar botón
                    this.disabled = false;
                    this.innerHTML = originalButtonHtml;
                });
        });
    });

    // Comprobar si las tablas están vacías al cargar para mostrar los mensajes correspondientes
    const tablaRecibidas = document.getElementById('tablaSolicitudesRecibidas');
    const mensajeVacioRecibidas = document.getElementById('mensajeVacioRecibidas');
    if (tablaRecibidas && mensajeVacioRecibidas && tablaRecibidas.tBodies[0] && tablaRecibidas.tBodies[0].rows.length === 0) {
        mensajeVacioRecibidas.style.display = 'block';
        tablaRecibidas.style.display = 'none';
    }

    const tablaEnviadas = document.getElementById('tablaSolicitudesEnviadas');
    const mensajeVacioEnviadas = document.getElementById('mensajeVacioEnviadas');
    if (tablaEnviadas && mensajeVacioEnviadas && tablaEnviadas.tBodies[0] && tablaEnviadas.tBodies[0].rows.length === 0) {
        mensajeVacioEnviadas.style.display = 'block';
        tablaEnviadas.style.display = 'none';
    }
});