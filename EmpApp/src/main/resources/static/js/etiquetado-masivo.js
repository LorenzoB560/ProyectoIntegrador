document.addEventListener('DOMContentLoaded', () => {
    // Validar que jefeId existe y no es el valor por defecto
    if (typeof jefeId === 'undefined' || jefeId === 'default-jefe-id') {
        mostrarMensaje('Error: No se pudo obtener el ID del jefe. La página no puede funcionar.', 'error-message');
        console.error("Error: jefeId no está definido o tiene el valor por defecto.");
        // Deshabilitar botones para evitar errores
        document.getElementById('btnEnviarEtiquetado').disabled = true;
        return; // Detener la ejecución si no hay jefeId
    }

    cargarSubordinados();
    cargarEtiquetas();

    document.getElementById('btnEnviarEtiquetado').addEventListener('click', enviarEtiquetadoMasivo);
});

// Función para cargar subordinados usando AJAX (llama al endpoint de Empleado)
function cargarSubordinados() {
    const selectEmpleados = document.getElementById('empleadosDisponibles');
    selectEmpleados.innerHTML = '<option>Cargando...</option>';

    // *** URL CORRECTA: Apunta a EmpleadoRestController ***
    fetch(`/empleados/${jefeId}/subordinados`)
        .then(response => {
            if (!response.ok) {
                // Si el jefe no se encuentra (404) u otro error
                return response.text().then(text => { throw new Error(`Error ${response.status}: ${text || response.statusText}`) });
            }
            return response.json();
        })
        .then(subordinados => {
            selectEmpleados.innerHTML = ''; // Limpiar
            if (!subordinados || subordinados.length === 0) {
                selectEmpleados.innerHTML = '<option disabled>No tienes subordinados.</option>';
            } else {
                subordinados.sort((a, b) => {
                    const nombreA = `${a.apellido || ''}, ${a.nombre || ''}`.toLowerCase();
                    const nombreB = `${b.apellido || ''}, ${b.nombre || ''}`.toLowerCase();
                    return nombreA.localeCompare(nombreB);
                });
                subordinados.forEach(emp => {
                    const option = document.createElement('option');
                    option.value = emp.id;
                    option.textContent = `${emp.apellido || 'Sin Apellido'}, ${emp.nombre || 'Sin Nombre'}`;
                    selectEmpleados.appendChild(option);
                });
            }
        })
        .catch(error => {
            selectEmpleados.innerHTML = '<option disabled>Error al cargar</option>';
            mostrarMensaje('Error al cargar subordinados: ' + error.message, 'error-message');
            console.error(error);
        });
}

// Función para cargar etiquetas usando AJAX (llama al NUEVO endpoint de Etiqueta)
function cargarEtiquetas() {
    const selectEtiquetas = document.getElementById('etiquetasDisponibles');
    selectEtiquetas.innerHTML = '<option>Cargando...</option>';

    // *** Añadir log para verificar el jefeId usado en la URL ***
    console.log('Cargando etiquetas para jefeId:', jefeId);

    fetch(`/etiquetas/jefe/${jefeId}`)
        .then(response => {
            console.log('Respuesta fetch etiquetas status:', response.status); // Log status
            if (!response.ok) {
                return response.text().then(text => { throw new Error(`Error ${response.status}: ${text || response.statusText}`) });
            }
            return response.json();
        })
        .then(etiquetas => {
            // *** Añadir log para ver los datos recibidos ***
            console.log('Etiquetas recibidas del backend:', JSON.stringify(etiquetas, null, 2));

            selectEtiquetas.innerHTML = ''; // Limpiar
            if (!etiquetas || etiquetas.length === 0) {
                console.log('No se recibieron etiquetas o la lista está vacía.');
                selectEtiquetas.innerHTML = '<option disabled>No hay etiquetas disponibles creadas por este jefe.</option>';
            } else {
                console.log(`Procesando ${etiquetas.length} etiquetas...`);
                etiquetas.forEach((et, index) => {
                    // *** Añadir log dentro del bucle ***
                    console.log(`Procesando etiqueta #${index}: ID=${et.id}, Nombre=${et.nombre}`);
                    try {
                        const option = document.createElement('option');
                        option.value = et.id;
                        option.textContent = et.nombre;
                        selectEtiquetas.appendChild(option);
                        // *** Añadir log si la opción se añadió ***
                        // console.log(`Opción "${et.nombre}" añadida al select.`);
                    } catch (e) {
                        // *** Añadir log si hay error al crear/añadir la opción ***
                        console.error(`Error al procesar la etiqueta #${index} (${et.nombre}):`, e);
                    }
                });
                // *** Añadir log para ver el HTML final del select ***
                console.log('HTML final del select de etiquetas:', selectEtiquetas.innerHTML);
                // Reordenar después de añadir todas
                ordenarSelect(selectEtiquetas);
                console.log('Select de etiquetas ordenado.');
            }
        })
        .catch(error => {
            // *** Añadir log si hubo error en fetch o procesamiento ***
            console.error('Error en cargarEtiquetas:', error);
            selectEtiquetas.innerHTML = '<option disabled>Error al cargar etiquetas</option>';
            mostrarMensaje('Error al cargar etiquetas: ' + error.message, 'error-message');
        });
}
// Función genérica para mover opciones entre selects múltiples
function moverOpciones(origenId, destinoId, moverTodo) {
    const selectOrigen = document.getElementById(origenId);
    const selectDestino = document.getElementById(destinoId);
    const opcionesAMover = [];

    if (moverTodo) {
        // Mover todas las opciones no deshabilitadas
        for (let i = 0; i < selectOrigen.options.length; i++) {
            if(!selectOrigen.options[i].disabled) { // No mover opciones deshabilitadas (como "Cargando...")
                opcionesAMover.push(selectOrigen.options[i]);
            }
        }
    } else {
        // Mover solo las seleccionadas y no deshabilitadas
        for (let i = 0; i < selectOrigen.selectedOptions.length; i++) {
            if(!selectOrigen.selectedOptions[i].disabled) {
                opcionesAMover.push(selectOrigen.selectedOptions[i]);
            }
        }
    }

    opcionesAMover.forEach(option => {
        selectDestino.appendChild(option);
    });

    // Reordenar ambos selects después de mover
    ordenarSelect(selectDestino);
    ordenarSelect(selectOrigen);
}

// Función para ordenar alfabéticamente las opciones de un select
function ordenarSelect(selectElement) {
    const options = Array.from(selectElement.options);
    // Filtrar opciones deshabilitadas si existen (como "Cargando..." o mensajes de error)
    const disabledOptions = options.filter(opt => opt.disabled);
    const enabledOptions = options.filter(opt => !opt.disabled);

    enabledOptions.sort((a, b) => a.text.localeCompare(b.text));

    // Vaciar y re-agregar ordenado, manteniendo las deshabilitadas al principio si las había
    selectElement.innerHTML = "";
    disabledOptions.forEach(option => selectElement.appendChild(option));
    enabledOptions.forEach(option => selectElement.appendChild(option));
}


// Función para enviar los datos del etiquetado masivo (llama al NUEVO endpoint de Etiqueta)
function enviarEtiquetadoMasivo() {
    const empleadosSeleccionados = Array.from(document.getElementById('empleadosSeleccionados').options).map(opt => opt.value);
    const etiquetasSeleccionadas = Array.from(document.getElementById('etiquetasSeleccionadas').options).map(opt => opt.value);

    if (empleadosSeleccionados.length === 0) {
        mostrarMensaje('Error: Debe seleccionar al menos un empleado.', 'error-message');
        return;
    }
    if (etiquetasSeleccionadas.length === 0) {
        mostrarMensaje('Error: Debe seleccionar al menos una etiqueta.', 'error-message');
        return;
    }

    if (!confirm('¿Está seguro de que desea asignar las etiquetas seleccionadas a los empleados seleccionados?')) {
        return;
    }

    const payload = {
        jefeId: [jefeId], // El backend espera una lista aquí
        empleadoIds: empleadosSeleccionados,
        etiquetaIds: etiquetasSeleccionadas
    };

    mostrarMensaje('Procesando...', 'info-message'); // Feedback visual

    // *** URL CORRECTA: Apunta a EtiquetaRestController ***
    fetch('/etiquetas/asignar-masivo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // Incluir token CSRF si Spring Security está habilitado y requiere CSRF
            // 'X-CSRF-TOKEN': csrfToken // (Necesitarías obtener este token)
        },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (response.ok) { // Estatus 200-299
                return response.status; // O simplemente null/true si no esperas contenido
            } else {
                // Intentar leer el cuerpo del error para dar más detalles
                return response.text().then(text => {
                    const errorMsg = `Error ${response.status}: ${text || response.statusText}`;
                    throw new Error(errorMsg);
                });
            }
        })
        .then(status => {
            mostrarMensaje('Etiquetas asignadas correctamente.', 'success-message');
            // Considera si quieres resetear o recargar algo aquí
            // resetearFormulario(); // Podría ser útil
        })
        .catch(error => {
            mostrarMensaje('Error al asignar etiquetas: ' + error.message, 'error-message');
            console.error("Detalle del error:", error);
        });
}

// Función para mostrar mensajes de feedback
function mostrarMensaje(mensaje, claseCss) {
    const feedbackDiv = document.getElementById('feedback');
    feedbackDiv.textContent = mensaje;
    feedbackDiv.className = claseCss; // ej. 'error-message', 'success-message', 'info-message'
    feedbackDiv.style.display = 'block';

    // Ocultar mensaje después de un tiempo, excepto si es un error persistente
    if (!claseCss.includes('error-message')) {
        setTimeout(() => {
            if (feedbackDiv.textContent === mensaje) { // Ocultar solo si el mensaje no ha cambiado
                feedbackDiv.style.display = 'none';
                feedbackDiv.textContent = '';
                feedbackDiv.className = '';
            }
        }, 5000); // Ocultar después de 5 segundos
    }
}

// Función para resetear el formulario
function resetearFormulario() {
    // Mueve todas las opciones de vuelta a las listas de "disponibles"
    moverOpciones('empleadosSeleccionados', 'empleadosDisponibles', true);
    moverOpciones('etiquetasSeleccionadas', 'etiquetasDisponibles', true);
    // Limpiar mensajes
    const feedbackDiv = document.getElementById('feedback');
    feedbackDiv.style.display = 'none';
    feedbackDiv.textContent = '';
    feedbackDiv.className = '';
}