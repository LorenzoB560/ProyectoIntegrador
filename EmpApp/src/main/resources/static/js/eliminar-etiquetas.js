document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formEliminarEtiquetas');
    const selectEmpleado = document.getElementById('selectEmpleadoEliminar');
    const divEtiquetas = document.getElementById('listaEtiquetasActuales');
    const feedbackDiv = document.getElementById('feedbackEliminar');
    const errorEmpleadoDiv = document.getElementById('errorEmpleadoSeleccionado');
    const errorEtiquetasDiv = document.getElementById('errorEtiquetasSeleccionadas');
    const btnEnviar = document.getElementById('btnEnviarEliminar');

    const jefeId = form.getAttribute('data-jefe-id');

    if (!jefeId) {
        mostrarFeedback('Error crítico: No se pudo obtener el ID del jefe.', 'alert alert-danger');
        if(btnEnviar) btnEnviar.disabled = true;
        return;
    }

    // --- Cargar Empleados (Subordinados) ---
    fetch(`/empleados/${jefeId}/subordinados`)
        .then(response => response.ok ? response.json() : Promise.reject('Error al cargar subordinados'))
        .then(empleados => {
            // Limpiar opciones excepto la primera (- Elije -)
            selectEmpleado.length = 1;
            if (empleados.length > 0) {
                empleados.forEach(emp => {
                    const option = document.createElement('option');
                    option.value = emp.id;
                    option.textContent = `${emp.apellido || ''}, ${emp.nombre || ''}`;
                    selectEmpleado.appendChild(option);
                });
            } else {
                selectEmpleado.options[0].textContent = "- No hay subordinados -";
                selectEmpleado.disabled = true;
            }
        })
        .catch(error => {
            console.error('Error cargando subordinados:', error);
            selectEmpleado.options[0].textContent = "- Error al cargar -";
            mostrarFeedback('Error al cargar la lista de empleados.', 'alert alert-danger');
        });

    // --- Cargar Etiquetas al Seleccionar Empleado ---
    selectEmpleado.addEventListener('change', () => {
        const empleadoId = selectEmpleado.value;
        divEtiquetas.innerHTML = '<p class="text-muted">Cargando etiquetas...</p>'; // Feedback visual
        errorEmpleadoDiv.style.display = 'none'; // Ocultar error previo
        errorEtiquetasDiv.style.display = 'none';

        if (!empleadoId) {
            divEtiquetas.innerHTML = '<p class="text-muted">Selecciona un empleado para ver sus etiquetas.</p>';
            return;
        }

        // Usamos el endpoint de detalle que ya incluye las etiquetas
        fetch(`/empleados/detalle/${empleadoId}`)
            .then(response => response.ok ? response.json() : Promise.reject('Error al cargar detalles del empleado'))
            .then(empleadoDto => {
                divEtiquetas.innerHTML = ''; // Limpiar
                const etiquetas = empleadoDto.etiquetas || [];
                if (etiquetas.length === 0) {
                    divEtiquetas.innerHTML = '<p class="text-muted">Este empleado no tiene etiquetas asignadas.</p>';
                } else {
                    etiquetas.sort((a, b) => a.nombre.localeCompare(b.nombre)); // Ordenar alfabéticamente
                    etiquetas.forEach(et => {
                        // Crear un div para cada checkbox + label
                        const divCheck = document.createElement('div');
                        divCheck.className = 'form-check';

                        const checkbox = document.createElement('input');
                        checkbox.type = 'checkbox';
                        checkbox.className = 'form-check-input';
                        checkbox.value = et.id; // El valor será el ID de la etiqueta
                        checkbox.id = `etiqueta-${et.id}`; // ID único para el label
                        checkbox.name = 'etiquetasAEliminar'; // Nombre común

                        const label = document.createElement('label');
                        label.className = 'form-check-label';
                        label.htmlFor = checkbox.id;
                        label.textContent = et.nombre;

                        divCheck.appendChild(checkbox);
                        divCheck.appendChild(label);
                        divEtiquetas.appendChild(divCheck);
                    });
                }
            })
            .catch(error => {
                console.error('Error cargando etiquetas del empleado:', error);
                divEtiquetas.innerHTML = '<p class="text-danger">Error al cargar las etiquetas.</p>';
                mostrarFeedback('Error al obtener las etiquetas del empleado.', 'alert alert-danger');
            });
    });

    // --- Envío del Formulario (Eliminar Etiquetas Seleccionadas) ---
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        if(btnEnviar.disabled) return;

        // --- Validación ---
        const empleadoId = selectEmpleado.value;
        const checkboxesEtiquetas = divEtiquetas.querySelectorAll('input[name="etiquetasAEliminar"]:checked');
        const etiquetasIdsAEliminar = Array.from(checkboxesEtiquetas).map(cb => cb.value);

        let isValid = true;
        errorEmpleadoDiv.style.display = 'none';
        errorEtiquetasDiv.style.display = 'none';


        if (!empleadoId) {
            errorEmpleadoDiv.textContent = 'Debe seleccionar un empleado.';
            errorEmpleadoDiv.style.display = 'block';
            isValid = false;
        }
        if (etiquetasIdsAEliminar.length === 0) {
            errorEtiquetasDiv.textContent = 'Debe marcar al menos una etiqueta para eliminar.';
            errorEtiquetasDiv.style.display = 'block';
            isValid = false;
        }
        if (!isValid) return;


        // --- Confirmación ---
        if (!confirm(`¿Está seguro de eliminar ${etiquetasIdsAEliminar.length} etiqueta(s) para el empleado seleccionado?`)) {
            return;
        }

        // --- Envío AJAX (Bucle DELETE) ---
        mostrarFeedback('Procesando eliminación...', 'alert alert-info', false);
        btnEnviar.disabled = true;

        let errores = 0;
        const totalAEliminar = etiquetasIdsAEliminar.length;

        // Usamos un bucle con await para enviar las peticiones DELETE una por una
        for (const etiquetaId of etiquetasIdsAEliminar) {
            try {
                const response = await fetch(`/etiquetas/eliminar/empleado/${empleadoId}/etiqueta/${etiquetaId}?jefeId=${jefeId}`, {
                    method: 'DELETE',
                    headers: {
                        // Añadir CSRF si es necesario
                    }
                });
                if (!response.ok) {
                    // Si falla una, contamos el error y continuamos con las demás
                    console.error(`Error ${response.status} eliminando etiqueta ${etiquetaId}`);
                    errores++;
                } else {
                    console.log(`Etiqueta ${etiquetaId} eliminada para empleado ${empleadoId}`);
                }
            } catch (error) {
                console.error(`Error de red eliminando etiqueta ${etiquetaId}:`, error);
                errores++;
            }
        }

        // --- Mostrar Resultado Final ---
        if (errores === 0) {
            mostrarFeedback(`Se eliminaron ${totalAEliminar} etiqueta(s) correctamente.`, 'alert alert-success');
            // Recargar etiquetas para el empleado actual
            selectEmpleado.dispatchEvent(new Event('change')); // Simula el evento change para recargar
        } else {
            mostrarFeedback(`Se eliminaron ${totalAEliminar - errores} de ${totalAEliminar} etiquetas. Hubo ${errores} errores.`, 'alert alert-warning');
            // Igualmente recargamos para ver el estado actual
            selectEmpleado.dispatchEvent(new Event('change'));
        }
        btnEnviar.disabled = false;
    });

    // Función para mostrar mensajes
    function mostrarFeedback(mensaje, claseCss, autoOcultar = true) {
        feedbackDiv.textContent = mensaje;
        feedbackDiv.className = claseCss;
        feedbackDiv.style.display = 'block';

        if (autoOcultar) {
            setTimeout(() => {
                if (feedbackDiv.textContent === mensaje) {
                    feedbackDiv.style.display = 'none';
                    feedbackDiv.textContent = '';
                    feedbackDiv.className = '';
                }
            }, 5000);
        }
    }
});