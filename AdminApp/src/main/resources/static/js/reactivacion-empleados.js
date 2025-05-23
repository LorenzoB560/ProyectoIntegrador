// En ProyectoIntegrador/AdminApp/src/main/resources/static/js/reactivacion-empleados.js

document.addEventListener('DOMContentLoaded', function () {
    const cuerpoTabla = document.getElementById('cuerpoTablaInactivos');
    const tablaEmpleados = document.getElementById('tablaEmpleadosInactivos');
    const cargandoDiv = document.getElementById('cargando');
    const errorDiv = document.getElementById('error');
    const statusMessagesDiv = document.getElementById('statusMessages');
    const noHayInactivosDiv = document.getElementById('noHayInactivos');

    const btnSeleccionarTodos = document.getElementById('seleccionarTodos');
    const btnDeseleccionarTodos = document.getElementById('deseleccionarTodos');
    const btnReactivarSeleccionados = document.getElementById('reactivarSeleccionados');
    const checkboxGeneral = document.getElementById('checkboxGeneral');
    const contadorSeleccionadosSpan = document.getElementById('contadorSeleccionados');

    let todosLosEmpleadosCargados = []; // Almacenará los empleados cargados

    function mostrarError(mensaje) {
        errorDiv.textContent = mensaje;
        errorDiv.style.display = 'block';
        cargandoDiv.style.display = 'none';
    }

    function limpiarMensajes() {
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';
        statusMessagesDiv.innerHTML = '';
    }

    function agregarMensajeEstado(mensaje, tipo = 'info', permanente = false) {
        const alertClass = tipo === 'danger' ? 'alert-danger' : (tipo === 'success' ? 'alert-success' : (tipo === 'warning' ? 'alert-warning' : 'alert-info'));
        const mensajeDiv = document.createElement('div');
        mensajeDiv.className = `alert ${alertClass} alert-dismissible fade show small`;
        mensajeDiv.setAttribute('role', 'alert');
        mensajeDiv.innerHTML = `${mensaje}`;
        if (!permanente) {
            mensajeDiv.innerHTML += `<button type="button" class="btn-close btn-sm" data-bs-dismiss="alert" aria-label="Close"></button>`;
        }
        statusMessagesDiv.appendChild(mensajeDiv);
    }

    function actualizarContadorSeleccionados() {
        const seleccionados = document.querySelectorAll('.empleado-checkbox:checked').length;
        contadorSeleccionadosSpan.textContent = seleccionados;
    }

    function cargarEmpleadosInactivos() {
        limpiarMensajes();
        cargandoDiv.style.display = 'block';
        tablaEmpleados.style.display = 'none';
        noHayInactivosDiv.style.display = 'none';
        btnReactivarSeleccionados.disabled = true;

        const url = new URL('http://localhost:9090/empleados/todos-inactivos');

        fetch(url)
            .then(response => {
                if (response.status === 204) {
                    return [];
                }
                if (!response.ok) {
                    return response.text().then(text => { throw new Error('Error cargando empleados inactivos: ' + response.status + " " + text) });
                }
                return response.json();
            })
            .then(listaEmpleados => {
                cargandoDiv.style.display = 'none';
                todosLosEmpleadosCargados = listaEmpleados; // Guardar la lista completa
                renderizarTabla(todosLosEmpleadosCargados);

                if (!todosLosEmpleadosCargados || todosLosEmpleadosCargados.length === 0) {
                    noHayInactivosDiv.style.display = 'block';
                    tablaEmpleados.style.display = 'none';
                    btnSeleccionarTodos.disabled = true;
                    btnDeseleccionarTodos.disabled = true;
                    checkboxGeneral.disabled = true;
                } else {
                    tablaEmpleados.style.display = 'table';
                    noHayInactivosDiv.style.display = 'none';
                    btnSeleccionarTodos.disabled = false;
                    btnDeseleccionarTodos.disabled = false;
                    checkboxGeneral.disabled = false;
                    btnReactivarSeleccionados.disabled = false;
                }
                actualizarContadorSeleccionados();
            })
            .catch(error => {
                console.error('Error:', error);
                mostrarError(error.message);
                btnSeleccionarTodos.disabled = true;
                btnDeseleccionarTodos.disabled = true;
                checkboxGeneral.disabled = true;
            });
    }

    function renderizarTabla(empleados) {
        cuerpoTabla.innerHTML = '';
        if (!empleados || empleados.length === 0) {
            checkboxGeneral.checked = false;
            return;
        }
        empleados.forEach(emp => {
            const fila = cuerpoTabla.insertRow();
            fila.setAttribute('data-employee-id', emp.id);

            const cellCheckbox = fila.insertCell();
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'form-check-input empleado-checkbox';
            checkbox.value = emp.id;
            checkbox.setAttribute('aria-label', `Seleccionar empleado ${emp.nombre} ${emp.apellido}`);
            cellCheckbox.appendChild(checkbox);

            const cellNombre = fila.insertCell();
            cellNombre.textContent = `${emp.nombre || ''} ${emp.apellido || ''}`.trim() || 'N/A';

            const cellCorreo = fila.insertCell();
            cellCorreo.textContent = emp.usuario.usuario || 'N/A';

            const cellDepto = fila.insertCell();
            cellDepto.textContent = (emp.departamento && emp.departamento.nombre) || 'N/A';

            const cellFechaCese = fila.insertCell();
            cellFechaCese.textContent = (emp.periodo && emp.periodo.fechaFin) ? formatearFecha(emp.periodo.fechaFin) : 'N/A';

            const cellId = fila.insertCell(); // Mostrando el ID para referencia
            cellId.textContent = emp.id;
        });
        checkboxGeneral.checked = false;
        actualizarContadorSeleccionados();
    }

    function formatearFecha(textoFecha) {
        if (!textoFecha) return 'N/A';
        try {
            const fecha = new Date(textoFecha);
            if (isNaN(fecha.getTime())) return 'Fecha Inválida';
            return fecha.toLocaleDateString('es-ES', { year: 'numeric', month: '2-digit', day: '2-digit' });
        } catch (e) {
            return 'N/A';
        }
    }

    btnSeleccionarTodos.addEventListener('click', () => {
        document.querySelectorAll('.empleado-checkbox').forEach(cb => cb.checked = true);
        checkboxGeneral.checked = true;
        actualizarContadorSeleccionados();
    });

    btnDeseleccionarTodos.addEventListener('click', () => {
        document.querySelectorAll('.empleado-checkbox').forEach(cb => cb.checked = false);
        checkboxGeneral.checked = false;
        actualizarContadorSeleccionados();
    });

    checkboxGeneral.addEventListener('change', function() {
        document.querySelectorAll('.empleado-checkbox').forEach(cb => cb.checked = this.checked);
        actualizarContadorSeleccionados();
    });

    cuerpoTabla.addEventListener('change', function(event) {
        if (event.target.classList.contains('empleado-checkbox')) {
            const checkboxesVisibles = document.querySelectorAll('.empleado-checkbox');
            const seleccionadosVisibles = document.querySelectorAll('.empleado-checkbox:checked');
            checkboxGeneral.checked = checkboxesVisibles.length > 0 && checkboxesVisibles.length === seleccionadosVisibles.length;
            actualizarContadorSeleccionados();
        }
    });

    btnReactivarSeleccionados.addEventListener('click', async function() {
        const checkboxesSeleccionados = Array.from(document.querySelectorAll('.empleado-checkbox:checked'));
        const idsParaReactivar = checkboxesSeleccionados.map(cb => cb.value);

        if (idsParaReactivar.length === 0) {
            agregarMensajeEstado('Por favor, selecciona al menos un empleado para reactivar.', 'warning');
            return;
        }

        if (!confirm(`¿Estás seguro de que quieres reactivar a ${idsParaReactivar.length} empleado(s) seleccionado(s)?`)) {
            return;
        }

        this.disabled = true;
        this.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Reactivando (${idsParaReactivar.length})...`;
        limpiarMensajes();

        let reactivadosConExito = 0;
        let erroresReactivacion = 0;
        const promesas = [];

        agregarMensajeEstado(`Iniciando proceso de reactivación para ${idsParaReactivar.length} empleado(s)...`, 'info', true);

        for (const id of idsParaReactivar) {
            const empleadoData = todosLosEmpleadosCargados.find(emp => emp.id === id);
            const nombreEmpleado = empleadoData ? `${empleadoData.nombre || ''} ${empleadoData.apellido || ''}`.trim() : `ID ${id}`;

            const promesa = fetch(`/empleados/${id}/activar`, { // Endpoint individual
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // CSRF token
                }
            })
                .then(async response => {
                    const responseText = await response.text();
                    if (response.ok) {
                        agregarMensajeEstado(`OK: ${nombreEmpleado} reactivado. (${responseText || 'Éxito'})`, 'success');
                        reactivadosConExito++;
                    } else {
                        agregarMensajeEstado(`Error: ${nombreEmpleado}. ${responseText || response.statusText}`, 'danger');
                        erroresReactivacion++;
                    }
                })
                .catch(error => {
                    console.error(`Error en fetch para empleado ID ${id}:`, error);
                    agregarMensajeEstado(`Error de Red: ${nombreEmpleado}. ${error.message}`, 'danger');
                    erroresReactivacion++;
                });
            promesas.push(promesa);
        }

        await Promise.allSettled(promesas);

        this.disabled = false;
        this.innerHTML = '<i class="bi bi-person-check-fill"></i> Reactivar Seleccionados';

        let mensajeFinal = `Proceso de reactivación completado. Reactivados: ${reactivadosConExito}. Errores: ${erroresReactivacion}.`;
        agregarMensajeEstado(mensajeFinal, erroresReactivacion > 0 ? 'warning' : 'info', true);

        if (reactivadosConExito > 0) {
            agregarMensajeEstado("Actualizando lista de empleados inactivos...", "info", true);
            setTimeout(() => {
                cargarEmpleadosInactivos();
            }, 2000);
        }
        checkboxGeneral.checked = false;
        actualizarContadorSeleccionados();
    });

    // Carga inicial
    cargarEmpleadosInactivos();
});