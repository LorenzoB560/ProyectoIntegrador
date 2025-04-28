document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formNuevoEtiquetado');
    const selectEmpleados = document.getElementById('empleadosSubordinados');
    const inputEtiqueta = document.getElementById('etiquetaInput');
    const listaSugerencias = document.getElementById('sugerenciasEtiquetas');
    const feedbackDiv = document.getElementById('feedback');
    const errorEmpleadosDiv = document.getElementById('errorEmpleados');
    const errorEtiquetaDiv = document.getElementById('errorEtiqueta');
    const btnEnviar = document.getElementById('btnEnviarNuevoEtiquetado');

    // Obtener jefeId del atributo data-*
    const jefeId = form.getAttribute('data-jefe-id');

    if (!jefeId) {
        mostrarFeedback('Error crítico: No se pudo obtener el ID del jefe.', 'alert alert-danger');
        if(btnEnviar) btnEnviar.disabled = true;
        return;
    }

    // --- Cargar Subordinados ---
    fetch(`/empleados/${jefeId}/subordinados`)
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar subordinados');
            return response.json();
        })
        .then(empleados => {
            selectEmpleados.innerHTML = ''; // Limpiar opción "Cargando..."
            if (empleados.length === 0) {
                selectEmpleados.innerHTML = '<option disabled>No hay subordinados.</option>';
                selectEmpleados.disabled = true;
            } else {
                // Ordenar por Apellido, Nombre (ya debería venir ordenado del backend)
                empleados.forEach(emp => {
                    const option = document.createElement('option');
                    option.value = emp.id; // ID del empleado
                    option.textContent = `${emp.apellido || 'Sin Apellido'}, ${emp.nombre || 'Sin Nombre'}`;
                    selectEmpleados.appendChild(option);
                });
                selectEmpleados.disabled = false;
            }
        })
        .catch(error => {
            console.error('Error cargando subordinados:', error);
            selectEmpleados.innerHTML = '<option disabled>Error al cargar</option>';
            mostrarFeedback('Error al cargar la lista de empleados.', 'alert alert-danger');
        });

    // --- Sugerencias de Etiquetas ---
    let debounceTimeout;
    inputEtiqueta.addEventListener('keyup', () => {
        clearTimeout(debounceTimeout);
        const prefijo = inputEtiqueta.value.trim();

        if (prefijo.length === 0) { // O > 1 si prefieres
            listaSugerencias.innerHTML = ''; // Limpiar si está vacío
            return;
        }

        debounceTimeout = setTimeout(() => {
            fetch(`/etiquetas/jefe/<span class="math-inline">\{jefeId\}/sugerencias?prefijo\=</span>{encodeURIComponent(prefijo)}`)
                .then(response => {
                    if (!response.ok) throw new Error('Error al buscar sugerencias');
                    return response.json();
                })
                .then(sugerencias => {
                    listaSugerencias.innerHTML = ''; // Limpiar anteriores
                    if (sugerencias.length > 0) {
                        sugerencias.forEach(et => {
                            const li = document.createElement('li');
                            li.textContent = et.nombre;
                            li.addEventListener('click', () => {
                                inputEtiqueta.value = et.nombre;
                                listaSugerencias.innerHTML = ''; // Ocultar al seleccionar
                            });
                            listaSugerencias.appendChild(li);
                        });
                    } else {
                        // Opcional: mostrar "No hay sugerencias"
                        // const li = document.createElement('li');
                        // li.textContent = 'No hay sugerencias';
                        // li.style.fontStyle = 'italic';
                        // li.style.color = '#6c757d';
                        // listaSugerencias.appendChild(li);
                    }
                })
                .catch(error => {
                    console.error('Error buscando sugerencias:', error);
                    listaSugerencias.innerHTML = ''; // Limpiar en caso de error
                });
        }, 300); // Espera 300ms después de dejar de teclear
    });

    // Ocultar sugerencias si se hace clic fuera
    document.addEventListener('click', (event) => {
        if (!inputEtiqueta.contains(event.target) && !listaSugerencias.contains(event.target)) {
            listaSugerencias.innerHTML = '';
        }
    });


    // --- Envío del Formulario ---
    form.addEventListener('submit', (event) => {
        event.preventDefault(); // Prevenir envío tradicional
        if(btnEnviar.disabled) return; // Evitar doble submit

        // --- Validación Frontend ---
        let isValid = true;
        errorEmpleadosDiv.style.display = 'none';
        errorEtiquetaDiv.style.display = 'none';

        const empleadosSeleccionados = Array.from(selectEmpleados.selectedOptions).map(opt => opt.value);
        const nombreEtiqueta = inputEtiqueta.value.trim();

        if (empleadosSeleccionados.length === 0) {
            errorEmpleadosDiv.textContent = 'Debe seleccionar al menos un empleado.';
            errorEmpleadosDiv.style.display = 'block';
            isValid = false;
        }

        if (nombreEtiqueta === '') {
            errorEtiquetaDiv.textContent = 'Debe introducir o seleccionar una etiqueta.';
            errorEtiquetaDiv.style.display = 'block';
            isValid = false;
        }

        if (!isValid) return;

        // --- Confirmación ---
        if (!confirm(`¿Está seguro de asignar/crear la etiqueta "${nombreEtiqueta}" a ${empleadosSeleccionados.length} empleado(s)?`)) {
            return;
        }

        // --- Envío AJAX ---
        mostrarFeedback('Procesando...', 'alert alert-info', false); // Mensaje temporal
        btnEnviar.disabled = true;

        const payload = {
            empleadoIds: empleadosSeleccionados,
            nombreEtiqueta: nombreEtiqueta
        };

        fetch(`/etiquetas/crearOAsignar?jefeId=${jefeId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Añadir cabecera CSRF si es necesario en tu configuración de Spring Security
                // 'X-CSRF-TOKEN': csrfToken // Obtén el token CSRF si lo usas
            },
            body: JSON.stringify(payload)
        })
            .then(response => {
                // Para obtener el texto del body incluso en errores
                return response.text().then(text => ({ ok: response.ok, status: response.status, text: text }));
            })
            .then(result => {
                if (result.ok) {
                    mostrarFeedback(result.text || 'Operación completada con éxito.', 'alert alert-success');
                    // Opcional: Resetear formulario tras éxito
                    // vaciarFormularioNuevoEtiquetado();
                } else {
                    // Intenta mostrar el mensaje de error del backend si existe
                    mostrarFeedback(`Error ${result.status}: ${result.text || 'No se pudo completar la operación.'}`, 'alert alert-danger');
                }
            })
            .catch(error => {
                console.error('Error en fetch:', error);
                mostrarFeedback('Error de red o del servidor al enviar los datos.', 'alert alert-danger');
            })
            .finally(() => {
                btnEnviar.disabled = false; // Habilitar botón de nuevo
            });
    });

    // Función para mostrar mensajes
    function mostrarFeedback(mensaje, claseCss, autoOcultar = true) {
        feedbackDiv.textContent = mensaje;
        feedbackDiv.className = claseCss; // Reemplaza clases anteriores
        feedbackDiv.style.display = 'block';

        if (autoOcultar) {
            setTimeout(() => {
                if (feedbackDiv.textContent === mensaje) { // Solo oculta si el mensaje no ha cambiado
                    feedbackDiv.style.display = 'none';
                    feedbackDiv.textContent = '';
                    feedbackDiv.className = '';
                }
            }, 5000); // Ocultar después de 5 segundos
        }
    }

});
// Función específica para vaciar este formulario si no usas la genérica
function vaciarFormularioNuevoEtiquetado() {
    document.getElementById('formNuevoEtiquetado').reset();
    document.getElementById('empleadosSubordinados').selectedIndex = -1; // Deselecciona
    document.getElementById('sugerenciasEtiquetas').innerHTML = ''; // Limpia sugerencias
    document.getElementById('errorEmpleados').style.display = 'none';
    document.getElementById('errorEtiqueta').style.display = 'none';
    document.getElementById('feedback').style.display = 'none';
    // Si usas la función genérica vaciarCampos de modificarCampos.js, asegúrate
    // que el form tenga id="formulario" o ajusta la llamada:
    // vaciarCampos('formNuevoEtiquetado'); // Asumiendo que vaciarCampos acepta un id
}