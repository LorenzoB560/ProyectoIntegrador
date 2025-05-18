document.addEventListener('DOMContentLoaded', function () {
    const historialContainer = document.getElementById('historialColaboracionesContainer');
    const mensajeAlertaDiv = document.getElementById('mensajeAlertaHistorial');
    const loadingSpinner = document.getElementById('loadingSpinnerHistorial');

    if (!historialContainer || !mensajeAlertaDiv || !loadingSpinner) {
        console.error('Faltan elementos DOM esenciales para el script de historial de colaboraciones.');
        return;
    }

    function mostrarMensaje(mensaje, tipo = 'danger') {
        mensajeAlertaDiv.innerHTML = ''; // Limpiar mensajes anteriores
        const wrapper = document.createElement('div');
        wrapper.innerHTML =
            `<div class="alert alert-${tipo} alert-dismissible fade show" role="alert">
                ${escapeHtml(mensaje)}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
             </div>`;
        mensajeAlertaDiv.appendChild(wrapper);
        // mensajeAlertaDiv.scrollIntoView({ behavior: 'smooth', block: 'start' }); // Opcional
    }

    // Función para escapar HTML y prevenir XSS simple
    function escapeHtml(unsafe) {
        if (unsafe === null || typeof unsafe === 'undefined') {
            return '';
        }
        return String(unsafe)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function formatearFechaHora(fechaISO) {
        if (!fechaISO) return 'N/A';
        try {
            const fecha = new Date(fechaISO);
            if (isNaN(fecha.getTime())) return 'Fecha inválida'; // Comprobar si la fecha es válida
            const dia = String(fecha.getDate()).padStart(2, '0');
            const mes = String(fecha.getMonth() + 1).padStart(2, '0');
            const anio = fecha.getFullYear();
            const horas = String(fecha.getHours()).padStart(2, '0');
            const minutos = String(fecha.getMinutes()).padStart(2, '0');
            return `${dia}/${mes}/${anio} ${horas}:${minutos}`;
        } catch (e) {
            console.error("Error formateando fecha y hora: ", fechaISO, e);
            return 'Error fecha';
        }
    }

    function formatearFecha(fechaISO) {
        if (!fechaISO) return 'N/A';
        try {
            const fecha = new Date(fechaISO);
            if (isNaN(fecha.getTime())) return 'Fecha inválida';
            const dia = String(fecha.getDate()).padStart(2, '0');
            const mes = String(fecha.getMonth() + 1).padStart(2, '0');
            const anio = fecha.getFullYear();
            return `${dia}/${mes}/${anio}`;
        } catch (e) {
            console.error("Error formateando fecha: ", fechaISO, e);
            return 'Error fecha';
        }
    }

    function crearCardItemHistorial(item) {
        const cardDiv = document.createElement('div');
        let cardContentHtml = '';
        let cardClasses = 'card shadow-sm mb-4';

        // Validar que 'item' y 'item.tipo' existan
        if (!item || typeof item.tipo === 'undefined' || item.tipo === null) {
            console.error('Item de historial inválido o sin tipo:', item);
            cardDiv.className = cardClasses + ' border-danger'; // Marcar el card con error
            cardContentHtml = `<div class="card-body"><p class="text-danger fw-bold">Error: Tipo de item desconocido o inválido.</p><pre>${escapeHtml(JSON.stringify(item, null, 2))}</pre></div>`;
            cardDiv.innerHTML = cardContentHtml;
            return cardDiv;
        }

        // Sanitizar datos antes de usarlos en innerHTML
        const nombreOtroEmpleado = escapeHtml(item.nombreOtroEmpleado || 'Desconocido');
        const estadoActual = escapeHtml(item.estadoActual || 'DESCONOCIDO'); // Default a DESCONOCIDO si es null/undefined
        const fechaEventoPrincipalFormateada = formatearFechaHora(item.fechaEventoPrincipal);
        const fechaCreacionColaboracionFormateada = formatearFecha(item.fechaCreacionColaboracion);


        if (item.tipo === "COLABORACION_ESTABLECIDA") {
            cardClasses += ` colaboracion-card ${item.actualmenteActiva ? '' : 'inactiva'}`;
            const periodosHtml = item.periodos && item.periodos.length > 0
                ? `<ul class="list-group list-group-flush">
                    ${item.periodos.map((periodo, index) => {
                    // Validar periodo y sus fechas
                    const fechaInicioFormateada = formatearFechaHora(periodo ? periodo.fechaInicio : null);
                    const fechaFinFormateada = periodo && periodo.fechaFin ? ` - <span>${formatearFechaHora(periodo.fechaFin)}</span>` : '';
                    const activoBadge = !(periodo && periodo.fechaFin) ? '<span class="badge bg-success ms-2">Actualmente Activo</span>' : '';
                    return `
                        <li class="list-group-item periodo-item d-flex justify-content-between align-items-center px-0 py-2">
                            <div>
                                <i class="fas fa-calendar-alt me-2 text-muted"></i>
                                <span>${fechaInicioFormateada}</span>
                                ${fechaFinFormateada}
                                ${activoBadge}
                            </div>
                            <small class="text-muted">Periodo ${index + 1}</small>
                        </li>`;
                }).join('')}
                  </ul>`
                : '<p class="text-muted mb-0">No se han registrado periodos de actividad.</p>';

            const botonMensajeHtml = item.actualmenteActiva
                ? `<div class="card-footer bg-transparent border-top-0 text-end">
                     <a href="#" class="btn btn-sm btn-outline-primary disabled" title="Funcionalidad de mensajes no implementada aún">
                         <i class="fas fa-comments me-1"></i> Enviar Mensaje
                     </a>
                   </div>`
                : '';

            const estadoColaboracionBadge = item.actualmenteActiva ? '<span class="badge bg-light text-dark ms-2">ACTIVA</span>' : '<span class="badge bg-secondary ms-2">FINALIZADA</span>';

            cardContentHtml = `
                <div class="card-header ${item.actualmenteActiva ? 'bg-primary text-white' : 'bg-light'}">
                    <div class="d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            Colaboración con: <strong>${nombreOtroEmpleado}</strong>
                            ${estadoColaboracionBadge}
                        </h5>
                        <small class="${item.actualmenteActiva ? 'text-white-50' : 'text-muted'}">
                            Establecida: <span>${fechaCreacionColaboracionFormateada}</span>
                        </small>
                    </div>
                </div>
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">Periodos de Actividad:</h6>
                    ${periodosHtml}
                </div>
                ${botonMensajeHtml}
            `;
        } else if (item.tipo === "SOLICITUD_ENVIADA" || item.tipo === "SOLICITUD_RECIBIDA") {
            cardClasses += ` solicitud-card estado-${estadoActual.toLowerCase()}`;
            let tituloTipo = item.tipo === "SOLICITUD_ENVIADA" ? "Solicitud Enviada a:" : "Solicitud Recibida de:";
            let iconoTipo = item.tipo === "SOLICITUD_ENVIADA" ? "fa-paper-plane" : "fa-inbox";

            let accionesHtml = '';
            if (item.tipo === "SOLICITUD_RECIBIDA" && estadoActual.toUpperCase() === "PENDIENTE") {
                // Usar item.idReferencia que debe ser el ID de la SolicitudColaboracion
                accionesHtml = `
                    <div class="mt-3 text-end">
                        <button type="button" class="btn btn-success btn-sm me-1 btn-accion-solicitud-historial" data-solicitud-id="${item.idReferencia}" data-accion="aceptar">
                            <i class="fas fa-check me-1"></i>Aceptar
                        </button>
                        <button type="button" class="btn btn-danger btn-sm btn-accion-solicitud-historial" data-solicitud-id="${item.idReferencia}" data-accion="rechazar">
                            <i class="fas fa-times me-1"></i>Rechazar
                        </button>
                    </div>`;
            }

            cardContentHtml = `
                <div class="card-header bg-light">
                     <div class="d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                           <i class="fas ${iconoTipo} me-2 text-info"></i> ${tituloTipo} <strong>${nombreOtroEmpleado}</strong>
                        </h5>
                        <small class="text-muted">
                            Solicitada: <span>${fechaEventoPrincipalFormateada}</span>
                        </small>
                    </div>
                </div>
                <div class="card-body">
                    <p class="mb-1">Estado: <span class="badge estado-${estadoActual.toUpperCase()}">${estadoActual}</span></p>
                    ${accionesHtml}
                </div>
            `;
        } else { // Esto captura el "Tipo de item desconocido"
            console.warn('Renderizando card para tipo desconocido:', item.tipo, item);
            cardContentHtml = `<div class="card-body"><p class="text-danger fw-bold">Tipo de item desconocido: ${escapeHtml(item.tipo)}</p><pre>${escapeHtml(JSON.stringify(item, null, 2))}</pre></div>`;
            cardClasses += ' border-warning';
        }
        cardDiv.className = cardClasses;
        cardDiv.innerHTML = cardContentHtml;
        return cardDiv;
    }

    // Función global para manejar las acciones, ahora se adjuntarán los listeners de forma más segura
    async function gestionarSolicitud(idSolicitud, accion, boton) {
        console.log(`Gestionando solicitud: ${idSolicitud}, Accion: ${accion}`);
        const originalButtonHtml = boton.innerHTML;
        boton.disabled = true;
        boton.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>`;

        const apiUrl = `/api/empapp/colaboraciones/solicitudes/${idSolicitud}/${accion}`;
        try {
            const response = await fetch(apiUrl, { method: 'POST' });
            // Leer el cuerpo de la respuesta una sola vez
            const responseText = await response.text();
            let data;
            try {
                data = JSON.parse(responseText);
            } catch (e) {
                // Si no es JSON, podría ser un error HTML o texto plano
                console.error("La respuesta del servidor no es JSON:", responseText);
                throw new Error(response.ok ? "Respuesta no JSONinesperada del servidor." : `Error ${response.status}: ${responseText || response.statusText}`);
            }

            if (!response.ok) throw new Error(data.error || `Error ${response.status}`);

            mostrarMensaje(data.mensaje || `${accion.charAt(0).toUpperCase() + accion.slice(1)} exitosa.`, 'success');
            cargarHistorial(); // Recargar todo el historial para reflejar cambios.
        } catch (error) {
            mostrarMensaje(`Error al ${accion} la solicitud: ${error.message}`, 'danger');
            boton.disabled = false;
            boton.innerHTML = originalButtonHtml;
        }
    }

    // Delegación de eventos para los botones de acción
    historialContainer.addEventListener('click', function(event) {
        const boton = event.target.closest('.btn-accion-solicitud-historial');
        if (boton) {
            const idSolicitud = boton.dataset.solicitudId;
            const accion = boton.dataset.accion;
            if (idSolicitud && accion) {
                gestionarSolicitud(idSolicitud, accion, boton);
            }
        }
    });

    async function cargarHistorial() {
        loadingSpinner.style.display = 'block';
        historialContainer.innerHTML = '';
        mensajeAlertaDiv.innerHTML = '';

        try {
            console.log("Iniciando carga de historial...");
            const response = await fetch('/api/empapp/colaboraciones/historial');

            // Verificar si la respuesta es OK antes de intentar parsear JSON
            if (!response.ok) {
                let errorMsg = `Error ${response.status}: ${response.statusText}`;
                try {
                    const errorData = await response.json();
                    errorMsg = errorData.error || errorMsg;
                } catch(e) {
                    // Si no se puede parsear el error como JSON, usar el status text
                }
                throw new Error(errorMsg);
            }

            const data = await response.json();
            console.log("Datos recibidos del historial:", data); // LOG IMPORTANTE

            if (data && data.length > 0) {
                data.forEach(item => {
                    if (item) { // Comprobar que el item no sea null
                        historialContainer.appendChild(crearCardItemHistorial(item));
                    } else {
                        console.warn("Item nulo encontrado en la lista del historial.");
                    }
                });
            } else {
                historialContainer.innerHTML =
                    `<div class="alert alert-info mt-3" role="alert">
                        <i class="fas fa-info-circle me-2"></i>No tienes colaboraciones ni solicitudes en tu historial.
                     </div>`;
            }
        } catch (error) {
            console.error('Error al cargar el historial:', error);
            mostrarMensaje('No se pudo cargar el historial de colaboraciones. ' + error.message, 'danger');
        } finally {
            loadingSpinner.style.display = 'none';
        }
    }

    cargarHistorial();
});