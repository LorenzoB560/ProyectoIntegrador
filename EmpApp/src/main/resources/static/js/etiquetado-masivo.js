document.addEventListener('DOMContentLoaded', () => {
    // Validar que jefeId existe y no es el valor por defecto
    if (typeof jefeId === 'undefined' || !jefeId || jefeId === 'default-jefe-id') {
        mostrarMensaje('Error: No se pudo obtener el ID del jefe. La página no puede funcionar.', 'error-message');
        console.error("Error: jefeId no está definido o tiene el valor por defecto.");
        // Deshabilitar botones para evitar errores
        const btnEnviar = document.getElementById('btnEnviarEtiquetado');
        if(btnEnviar) btnEnviar.disabled = true;
        return; // Detener la ejecución si no hay jefeId
    }

    cargarSubordinados();
    cargarEtiquetas();

    // Asignar evento al botón de enviar
    const btnEnviar = document.getElementById('btnEnviarEtiquetado');
    if(btnEnviar) {
        btnEnviar.addEventListener('click', enviarEtiquetadoMasivo);
    } else {
        console.error("Error: Botón con ID 'btnEnviarEtiquetado' no encontrado.");
    }

    // Asignar evento al botón de reset (si existe)
    const btnReset = document.querySelector('button[onclick="resetearFormulario()"]'); // Otra forma de encontrarlo
    // o añade un ID al botón reset en el HTML y usa getElementById

});

// Función para cargar subordinados usando AJAX
function cargarSubordinados() {
    const selectEmpleados = document.getElementById('empleadosDisponibles');
    if (!selectEmpleados) {
        console.error("Error: Elemento con ID 'empleadosDisponibles' no encontrado.");
        return;
    }
    selectEmpleados.innerHTML = '<option disabled>Cargando...</option>';

    // URL correcta para obtener subordinados
    fetch(`/empleados/${jefeId}/subordinados`)
        .then(response => {
            if (!response.ok) {
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
            selectEmpleados.innerHTML = '<option disabled>Error al cargar subordinados</option>';
            mostrarMensaje('Error al cargar subordinados: ' + error.message, 'error-message');
            console.error(error);
        });
}

// Función para cargar etiquetas usando AJAX
function cargarEtiquetas() {
    const selectEtiquetas = document.getElementById('etiquetasDisponibles');
    if (!selectEtiquetas) {
        console.error("Error: Elemento con ID 'etiquetasDisponibles' no encontrado.");
        return;
    }
    selectEtiquetas.innerHTML = '<option disabled>Cargando...</option>';

    console.log('Cargando etiquetas para jefeId:', jefeId);

    // URL correcta para obtener etiquetas del jefe
    fetch(`/etiquetas/jefe/${jefeId}`) // Asegúrate que EtiquetaRestController tenga esta ruta
        .then(response => {
            console.log('Respuesta fetch etiquetas status:', response.status);
            if (!response.ok) {
                return response.text().then(text => { throw new Error(`Error ${response.status}: ${text || response.statusText}`) });
            }
            return response.json();
        })
        .then(etiquetas => {
            console.log('Etiquetas recibidas del backend:', JSON.stringify(etiquetas, null, 2));
            selectEtiquetas.innerHTML = ''; // Limpiar
            if (!etiquetas || etiquetas.length === 0) {
                console.log('No se recibieron etiquetas o la lista está vacía.');
                selectEtiquetas.innerHTML = '<option disabled>No hay etiquetas disponibles creadas por este jefe.</option>';
            } else {
                console.log(`Procesando ${etiquetas.length} etiquetas...`);
                etiquetas.forEach(et => { // Asume que el backend ya las ordena
                    const option = document.createElement('option');
                    option.value = et.id;
                    option.textContent = et.nombre;
                    selectEtiquetas.appendChild(option);
                });
                ordenarSelect(selectEtiquetas); // Ordenar por si acaso o si el backend no lo hace
            }
        })
        .catch(error => {
            console.error('Error en cargarEtiquetas:', error);
            selectEtiquetas.innerHTML = '<option disabled>Error al cargar etiquetas</option>';
            mostrarMensaje('Error al cargar etiquetas: ' + error.message, 'error-message');
        });
}

// Función genérica para mover opciones entre selects múltiples
function moverOpciones(origenId, destinoId, moverTodo) {
    const selectOrigen = document.getElementById(origenId);
    const selectDestino = document.getElementById(destinoId);
    if (!selectOrigen || !selectDestino) {
        console.error("Error: Select de origen o destino no encontrado para mover opciones.");
        return;
    }
    const opcionesAMover = [];

    if (moverTodo) {
        for (let i = 0; i < selectOrigen.options.length; i++) {
            if (!selectOrigen.options[i].disabled) {
                opcionesAMover.push(selectOrigen.options[i]);
            }
        }
    } else {
        for (let i = 0; i < selectOrigen.selectedOptions.length; i++) {
            if (!selectOrigen.selectedOptions[i].disabled) {
                opcionesAMover.push(selectOrigen.selectedOptions[i]);
            }
        }
    }

    opcionesAMover.forEach(option => {
        selectDestino.appendChild(option);
    });

    ordenarSelect(selectDestino);
    ordenarSelect(selectOrigen);
}

// Función para ordenar alfabéticamente las opciones de un select
function ordenarSelect(selectElement) {
    if (!selectElement) return;
    try {
        const options = Array.from(selectElement.options);
        const disabledOptions = options.filter(opt => opt.disabled);
        const enabledOptions = options.filter(opt => !opt.disabled);

        enabledOptions.sort((a, b) => a.text.localeCompare(b.text));

        selectElement.innerHTML = "";
        disabledOptions.forEach(option => selectElement.appendChild(option));
        enabledOptions.forEach(option => selectElement.appendChild(option));
    } catch (e) {
        console.error("Error al ordenar select:", e);
    }
}


async function enviarEtiquetadoMasivo() {
    const empleadosSeleccionados = Array.from(document.getElementById('empleadosSeleccionados').options).map(opt => opt.value);
    const etiquetasSeleccionadas = Array.from(document.getElementById('etiquetasSeleccionadas').options).map(opt => opt.value);
    const btnEnviar = document.getElementById('btnEnviarEtiquetado');

    if (empleadosSeleccionados.length === 0) {
        mostrarMensaje('Error: Debe seleccionar al menos un empleado.', 'error-message');
        return;
    }
    if (etiquetasSeleccionadas.length === 0) {
        mostrarMensaje('Error: Debe seleccionar al menos una etiqueta.', 'error-message');
        return;
    }

    // Confirmación mejorada mostrando el número de peticiones
    const numPeticiones = empleadosSeleccionados.length * etiquetasSeleccionadas.length;
    if (!confirm(`¿Está seguro? Esto realizará ${numPeticiones} petición(es) separada(s) para asignar las etiquetas.`)) {
        return;
    }

    mostrarMensaje(`Procesando ${numPeticiones} asignacion(es)...`, 'info-message');
    if(btnEnviar) btnEnviar.disabled = true; // Deshabilitar botón

    let exitos = 0;
    let fallos = 0;
    const erroresDetallados = [];

    // Usar Promise.all para ejecutar peticiones en paralelo (con límite opcional)
    // O mantener bucle secuencial con await si prefieres menos carga simultánea
    const promesas = [];

    for (const empleadoId of empleadosSeleccionados) {
        for (const etiquetaId of etiquetasSeleccionadas) {
            const url = `/etiquetas/asignar/empleado/${empleadoId}/etiqueta/${etiquetaId}?jefeId=${jefeId}`; // Incluir jefeId
            // Creamos una promesa para cada petición fetch
            const promesa = fetch(url, {
                method: 'PUT',
                headers: {
                }
            })
                .then(response => {
                    if (response.ok) {
                        exitos++;
                        console.log(`Éxito: Empleado ${empleadoId}, Etiqueta ${etiquetaId}`);
                        return { success: true }; // Indicar éxito
                    } else {
                        // Si falla, intentamos obtener el texto del error
                        return response.text().then(text => {
                            fallos++;
                            const errorMsg = `Error ${response.status} asignando etiqueta ${etiquetaId.substring(0,8)}... a empleado ${empleadoId.substring(0,8)}...: ${text || response.statusText}`;
                            console.error(errorMsg);
                            erroresDetallados.push(`Emp-${empleadoId.substring(0,4)}/Etiq-${etiquetaId.substring(0,4)}: ${response.status}`);
                            return { success: false, error: errorMsg }; // Indicar fallo
                        });
                    }
                })
                .catch(error => {
                    // Error de red o fetch
                    fallos++;
                    const errorMsg = `Error de red asignando etiqueta ${etiquetaId.substring(0,8)}... a empleado ${empleadoId.substring(0,8)}...: ${error.message}`;
                    console.error(errorMsg);
                    erroresDetallados.push(`Emp-${empleadoId.substring(0,4)}/Etiq-${etiquetaId.substring(0,4)}: Red`);
                    return { success: false, error: errorMsg }; // Indicar fallo
                });
            promesas.push(promesa); // Agregamos la promesa al array
        }
    }

    // Esperamos a que todas las promesas (peticiones) se completen
    await Promise.all(promesas);

    // Mostrar resultado final
    if(btnEnviar) btnEnviar.disabled = false; // Habilitar botón
    if (fallos === 0) {
        mostrarMensaje(`Proceso completado. ${exitos} asignacion(es) realizada(s) correctamente.`, 'success-message');
    } else {
        // Mostrar solo los primeros errores para no saturar
        const maxErroresVisibles = 5;
        mostrarMensaje(`Proceso completado con ${fallos} error(es) de ${numPeticiones}. ${exitos} asignacion(es) exitosa(s). Primeros errores: ${erroresDetallados.slice(0, maxErroresVisibles).join('; ')}${erroresDetallados.length > maxErroresVisibles ? '...' : ''}`, 'error-message');
        console.error("Lista completa de errores detallados:", erroresDetallados);
    }
    // No se resetea el formulario automáticamente, el usuario puede querer reintentar fallos.
}


// Función para mostrar mensajes de feedback
function mostrarMensaje(mensaje, claseCss) {
    const feedbackDiv = document.getElementById('feedback');
    if (!feedbackDiv) {
        console.error("Error: Elemento con ID 'feedback' no encontrado para mostrar mensaje.");
        alert(mensaje); // Fallback a alert
        return;
    }
    feedbackDiv.textContent = mensaje;
    feedbackDiv.className = claseCss;
    feedbackDiv.style.display = 'block';

    // No ocultar automáticamente mensajes de error
    if (!claseCss.includes('error-message')) {
        setTimeout(() => {
            if (feedbackDiv.textContent === mensaje) {
                feedbackDiv.style.display = 'none';
                feedbackDiv.textContent = '';
                feedbackDiv.className = '';
            }
        }, 7000); // Duración un poco mayor para éxito/info
    }
}

// Función para resetear el formulario
function resetearFormulario() {
    // Mueve todas las opciones de vuelta a las listas de "disponibles"
    moverOpciones('empleadosSeleccionados', 'empleadosDisponibles', true);
    moverOpciones('etiquetasSeleccionadas', 'etiquetasDisponibles', true);
    // Limpiar mensajes
    const feedbackDiv = document.getElementById('feedback');
    if (feedbackDiv) {
        feedbackDiv.style.display = 'none';
        feedbackDiv.textContent = '';
        feedbackDiv.className = '';
    }
}