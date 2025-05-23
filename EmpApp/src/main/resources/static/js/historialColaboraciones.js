document.addEventListener('DOMContentLoaded', function () {
    const historialContainer = document.getElementById('historialColaboracionesContainer');
    const mensajeAlertaDiv = document.getElementById('mensajeAlertaHistorial');
    const loadingSpinner = document.getElementById('loadingSpinnerHistorial');

    // Verificar que los elementos principales del DOM existan
    if (!historialContainer || !mensajeAlertaDiv || !loadingSpinner) {
        console.error('Faltan elementos DOM esenciales para el script de historial de colaboraciones. IDs esperados: historialColaboracionesContainer, mensajeAlertaHistorial, loadingSpinnerHistorial');
        if(mensajeAlertaDiv) mensajeAlertaDiv.innerHTML = '<div class="alert alert-danger" role="alert">Error de configuración de la página. Contacte al administrador.</div>';
        return;
    }

    // --- Funciones de Utilidad ---
    function mostrarMensaje(mensaje, tipo = 'danger') {
        mensajeAlertaDiv.innerHTML = ''; // Limpiar mensajes anteriores
        const wrapper = document.createElement('div');
        wrapper.innerHTML =
            `<div class="alert alert-${tipo} alert-dismissible fade show" role="alert">
                ${escapeHtml(mensaje)}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
             </div>`;
        mensajeAlertaDiv.appendChild(wrapper);
    }

    function escapeHtml(unsafe) {
        if (unsafe === null || typeof unsafe === 'undefined') return '';
        return String(unsafe).replace(/[&<>"']/g, function (match) {
            return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' }[match];
        });
    }

    function formatearFechaHora(fechaISO) {
        if (!fechaISO) return 'N/A';
        try {
            const fecha = new Date(fechaISO);
            if (isNaN(fecha.getTime())) return 'Fecha inválida';
            return fecha.toLocaleString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
        } catch (e) { console.error("Error formateando fecha y hora: ", fechaISO, e); return 'Error fecha';}
    }

    function formatearFecha(fechaISO) {
        if (!fechaISO) return 'N/A';
        try {
            const fecha = new Date(fechaISO);
            if (isNaN(fecha.getTime())) return 'Fecha inválida';
            return fecha.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
        } catch (e) { console.error("Error formateando fecha: ", fechaISO, e); return 'Error fecha';}
    }

    // --- Creación Dinámica de Elementos del Historial ---
    function crearCardItemHistorial(item) {
        const cardDiv = document.createElement('div');
        let cardContentHtml = '';
        let cardClasses = 'card shadow-sm mb-4';

        if (!item || typeof item.tipo === 'undefined' || item.tipo === null) {
            console.error('Item de historial inválido o sin tipo:', item);
            cardDiv.className = cardClasses + ' border-danger';
            cardContentHtml = `<div class="card-body"><p class="text-danger fw-bold">Error: Datos del ítem de historial incompletos.</p><pre class="small">${escapeHtml(JSON.stringify(item, null, 2))}</pre></div>`;
            cardDiv.innerHTML = cardContentHtml;
            return cardDiv;
        }

        const nombreOtroEmpleado = escapeHtml(item.nombreOtroEmpleado || 'Desconocido');
        const estadoActualString = escapeHtml(item.estadoActual || 'DESCONOCIDO');
        const estadoActualParaBadge = estadoActualString.toUpperCase();
        const fechaEventoPrincipalFormateada = formatearFechaHora(item.fechaEventoPrincipal);

        if (item.tipo === "COLABORACION_ESTABLECIDA") {
            cardClasses += ` colaboracion-card ${item.actualmenteActiva ? 'bg-light' : 'border-secondary'}`;
            const fechaEstablecimiento = formatearFecha(item.fechaCreacionColaboracion || item.fechaEventoPrincipal);

            const periodosHtml = item.periodos && item.periodos.length > 0
                ? `<ul class="list-group list-group-flush">
                    ${item.periodos.map((periodo, index) => {
                    const fechaInicioFormateada = formatearFechaHora(periodo ? periodo.fechaInicio : null);
                    const fechaFinFormateada = periodo && periodo.fechaFin ? ` - <span class="text-muted">${formatearFechaHora(periodo.fechaFin)}</span>` : '';
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

            let accionesColaboracionHtml = '';
            // El campo item.puedeFinalizar debe ser true si la colaboración está activa
            // (esto se setea en el constructor del DTO HistorialColaboracionItemDTO en el backend)
            if (item.puedeFinalizar && item.actualmenteActiva) {
                accionesColaboracionHtml += `
                    <button type="button" class="btn btn-sm btn-outline-warning btn-finalizar-colaboracion" 
                            data-colaboracion-id="${item.idReferencia}" title="Finalizar periodo actual de colaboración">
                        <i class="fas fa-stop-circle me-1"></i>Finalizar Periodo
                    </button>`;
            } else if (!item.actualmenteActiva) {
                accionesColaboracionHtml += `<small class="text-muted fst-italic">Esta colaboración está inactiva. Para continuar, envía una nueva solicitud.</small>`;
            }

            const botonMensajeHtml = item.actualmenteActiva
                ? `<a href="#" class="btn btn-sm btn-outline-primary disabled ms-2" title="Funcionalidad de mensajes no implementada aún">
                     <i class="fas fa-comments me-1"></i> Mensaje
                   </a>`
                : '';

            const estadoColaboracionBadge = item.actualmenteActiva ? '<span class="badge bg-primary ms-2">ACTIVA</span>' : '<span class="badge bg-secondary ms-2">INACTIVA</span>';

            cardContentHtml = `
                <div class="card-header ${item.actualmenteActiva ? 'bg-primary-subtle' : 'bg-light'}">
                    <div class="d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            Colaboración con: <strong>${nombreOtroEmpleado}</strong>
                            ${estadoColaboracionBadge}
                        </h5>
                        <small class="text-muted">
                            Establecida: <span>${fechaEstablecimiento}</span>
                        </small>
                    </div>
                </div>
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">Periodos de Actividad:</h6>
                    ${periodosHtml}
                </div>
                <div class="card-footer bg-transparent border-top-0 text-end pt-3">
                    ${accionesColaboracionHtml}
                    ${botonMensajeHtml}
                </div>
            `;
        } else if (item.tipo === "SOLICITUD_ENVIADA" || item.tipo === "SOLICITUD_RECIBIDA") {
            cardClasses += ` solicitud-card estado-solicitud-${estadoActualString.toLowerCase()}`;
            let tituloTipo = item.tipo === "SOLICITUD_ENVIADA" ? "Solicitud Enviada a:" : "Solicitud Recibida de:";
            let iconoTipo = item.tipo === "SOLICITUD_ENVIADA" ? "fa-paper-plane" : "fa-inbox";

            let accionesHtml = '';
            if (item.tipo === "SOLICITUD_RECIBIDA" && estadoActualParaBadge === "PENDIENTE") {
                accionesHtml = `
                    <div class="mt-3 text-end">
                        <button type="button" class="btn btn-success btn-sm me-1 btn-accion-solicitud" data-solicitud-id="${item.idReferencia}" data-accion="aceptar">
                            <i class="fas fa-check me-1"></i>Aceptar
                        </button>
                        <button type="button" class="btn btn-danger btn-sm btn-accion-solicitud" data-solicitud-id="${item.idReferencia}" data-accion="rechazar">
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
                    <p class="mb-1">Estado: <span class="badge estado-${estadoActualParaBadge}">${estadoActualString}</span></p>
                    ${accionesHtml}
                </div>
            `;
        } else {
            console.warn('Renderizando card para tipo desconocido:', item.tipo, item);
            cardContentHtml = `<div class="card-body"><p class="text-danger fw-bold">Tipo de ítem desconocido: ${escapeHtml(item.tipo)}</p><pre class="small">${escapeHtml(JSON.stringify(item, null, 2))}</pre></div>`;
            cardClasses += ' border-warning';
        }
        cardDiv.className = cardClasses;
        cardDiv.innerHTML = cardContentHtml;
        return cardDiv;
    }

    // --- Funciones para Gestionar Acciones (AJAX) ---

    // Función genérica para peticiones fetch POST
    async function realizarPeticionPost(apiUrl, boton, accionNombre) {
        const originalButtonHtml = boton.innerHTML; // Guardar HTML original para restaurar íconos, etc.
        boton.disabled = true;
        boton.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Procesando...`;

        try {
            const response = await fetch(apiUrl, { method: 'POST' });
            const responseText = await response.text(); // Leer como texto primero
            let data;
            try {
                data = JSON.parse(responseText);
            } catch (e) {
                console.error(`Respuesta no JSON al ${accionNombre}:`, responseText);
                // Si la respuesta es OK pero no es JSON, podría ser un mensaje simple.
                // Si no es OK, es un error del servidor.
                if (response.ok && responseText) { // Si hay texto y fue OK, usar ese texto.
                    data = { mensaje: responseText };
                } else {
                    throw new Error(response.ok ? "Respuesta inesperada del servidor (no JSON)." : `Error ${response.status}: ${responseText || response.statusText}`);
                }
            }

            if (!response.ok) {
                // Usar data.error si está disponible, sino el mensaje de error de la excepción
                const errorMessage = data.error || (data.message && response.status >= 400 ? data.message : `Error ${response.status}`);
                throw new Error(errorMessage);
            }

            mostrarMensaje(data.mensaje || `${accionNombre.charAt(0).toUpperCase() + accionNombre.slice(1)} con éxito.`, 'success');
            cargarHistorial(); // Recargar el historial para reflejar el cambio
        } catch (error) {
            mostrarMensaje(`Error al ${accionNombre}: ${error.message}`, 'danger');
            boton.disabled = false;
            boton.innerHTML = originalButtonHtml; // Restaurar HTML original del botón
        }
    }

    function gestionarAccionSolicitud(idSolicitud, accion, boton) {
        realizarPeticionPost(
            `/api/empapp/colaboraciones/solicitudes/${idSolicitud}/${accion}`,
            boton,
            accion === 'aceptar' ? 'aceptar la solicitud' : 'rechazar la solicitud'
        );
    }

    function gestionarFinalizarColaboracion(idColaboracion, boton) {
        realizarPeticionPost(
            `/api/empapp/colaboraciones/${idColaboracion}/finalizar-periodo`,
            boton,
            'finalizar el periodo'
        );
    }

    // --- Delegación de Eventos ---
    historialContainer.addEventListener('click', function(event) {
        const targetButton = event.target.closest('button');
        if (!targetButton) return;

        if (targetButton.classList.contains('btn-accion-solicitud')) {
            const idSolicitud = targetButton.dataset.solicitudId;
            const accion = targetButton.dataset.accion;
            if (idSolicitud && accion) {
                gestionarAccionSolicitud(idSolicitud, accion, targetButton);
            }
        } else if (targetButton.classList.contains('btn-finalizar-colaboracion')) {
            const idColaboracion = targetButton.dataset.colaboracionId;
            if (idColaboracion) {
                if (confirm('¿Estás seguro de que quieres finalizar el periodo actual de esta colaboración? Para volver a colaborar, deberás enviar una nueva solicitud después de un breve periodo de espera (2 minutos).')) {
                    gestionarFinalizarColaboracion(idColaboracion, targetButton);
                }
                // No se rehabilita el botón aquí si el usuario cancela, porque la acción no se disparó.
                // La función realizarPeticionPost se encarga de rehabilitar en caso de error.
            }
        }
    });

    // --- Carga Inicial del Historial ---
    async function cargarHistorial() {
        loadingSpinner.style.display = 'block';
        historialContainer.innerHTML = '';
        mensajeAlertaDiv.innerHTML = '';

        try {
            // console.log("Iniciando carga de historial..."); // Descomentar para depurar
            const response = await fetch('/api/empapp/colaboraciones/historial');

            if (!response.ok) {
                let errorMsg = `Error ${response.status}: ${response.statusText}`;
                try {
                    const contentType = response.headers.get("content-type");
                    if (contentType && contentType.indexOf("application/json") !== -1) {
                        const errorData = await response.json();
                        errorMsg = errorData.error || errorMsg; // Usar error del JSON si está disponible
                    } else { // Si no es JSON, intentar leer como texto
                        const errorText = await response.text();
                        if(errorText) errorMsg = errorText; // Usar texto si hay, sino el statusText
                    }
                } catch(e) {
                    console.warn("No se pudo parsear el cuerpo de la respuesta de error:", e);
                }
                throw new Error(errorMsg);
            }

            const data = await response.json();
            console.log("Datos recibidos del historial:", data); // Descomentar para depurar

            if (data && data.length > 0) {
                data.forEach(item => {
                    if (item) {
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

    // Cargar el historial cuando la página esté lista
    cargarHistorial();
});