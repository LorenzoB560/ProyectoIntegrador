document.addEventListener('DOMContentLoaded', function () {
    const cuerpoTabla = document.getElementById('cuerpoTablaActivos');
    const tablaEmpleados = document.getElementById('tablaEmpleadosActivos');
    const cargandoDiv = document.getElementById('cargandoDes'); // ID específico
    const errorDiv = document.getElementById('errorDes'); // ID específico
    const statusMessagesDiv = document.getElementById('statusMessagesDesactivacion'); // ID específico
    const noHayActivosDiv = document.getElementById('noHayActivos');

    const btnSeleccionarTodos = document.getElementById('seleccionarTodosDes'); // ID específico
    const btnDeseleccionarTodos = document.getElementById('deseleccionarTodosDes'); // ID específico
    const btnDesactivarSeleccionados = document.getElementById('desactivarSeleccionados');
    const checkboxGeneral = document.getElementById('checkboxGeneralDes'); // ID específico
    const contadorSeleccionadosSpan = document.getElementById('contadorSeleccionadosDes'); // ID específico

    let todosLosEmpleadosCargados = [];

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
        const seleccionados = document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox:checked').length;
        contadorSeleccionadosSpan.textContent = seleccionados;
    }

    function cargarEmpleadosActivos() {
        limpiarMensajes();
        cargandoDiv.style.display = 'block';
        tablaEmpleados.style.display = 'none';
        noHayActivosDiv.style.display = 'none';
        btnDesactivarSeleccionados.disabled = true;

        const url = new URL('http://localhost:9090/empleados/todos-activos'); // ENDPOINT PARA ACTIVOS
        // Opcional: parámetros de orden si los implementaste en el backend
        // url.searchParams.append('sortBy', 'apellido');
        // url.searchParams.append('sortDir', 'asc');

        fetch(url)
            .then(response => {
                if (response.status === 204) { return []; }
                if (!response.ok) {
                    return response.text().then(text => { throw new Error('Error cargando empleados activos: ' + response.status + " " + text) });
                }
                return response.json();
            })
            .then(listaEmpleados => {
                cargandoDiv.style.display = 'none';
                todosLosEmpleadosCargados = listaEmpleados;
                renderizarTablaActivos(todosLosEmpleadosCargados);

                if (!todosLosEmpleadosCargados || todosLosEmpleadosCargados.length === 0) {
                    noHayActivosDiv.style.display = 'block';
                    tablaEmpleados.style.display = 'none';
                    btnSeleccionarTodos.disabled = true;
                    btnDeseleccionarTodos.disabled = true;
                    checkboxGeneral.disabled = true;
                } else {
                    tablaEmpleados.style.display = 'table';
                    noHayActivosDiv.style.display = 'none';
                    btnSeleccionarTodos.disabled = false;
                    btnDeseleccionarTodos.disabled = false;
                    checkboxGeneral.disabled = false;
                    btnDesactivarSeleccionados.disabled = false;
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

    function renderizarTablaActivos(empleados) {
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
            checkbox.className = 'form-check-input empleado-checkbox'; // Misma clase para reutilizar lógica
            checkbox.value = emp.id;
            checkbox.setAttribute('aria-label', `Seleccionar empleado ${emp.nombre} ${emp.apellido}`);
            cellCheckbox.appendChild(checkbox);

            const cellNombre = fila.insertCell();
            cellNombre.textContent = `${emp.nombre || ''} ${emp.apellido || ''}`.trim() || 'N/A';

            const cellCorreo = fila.insertCell();
            cellCorreo.textContent = emp.usuario.usuario || 'N/A';

            const cellDepto = fila.insertCell();
            cellDepto.textContent = (emp.departamento && emp.departamento.nombre) || 'N/A';

            const cellFechaContratacion = fila.insertCell(); // Cambiado de Fecha Cese
            cellFechaContratacion.textContent = (emp.periodo && emp.periodo.fechaInicio) ? formatearFecha(emp.periodo.fechaInicio) : 'N/A';

            const cellId = fila.insertCell();
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
        document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox').forEach(cb => cb.checked = true);
        checkboxGeneral.checked = true;
        actualizarContadorSeleccionados();
    });

    btnDeseleccionarTodos.addEventListener('click', () => {
        document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox').forEach(cb => cb.checked = false);
        checkboxGeneral.checked = false;
        actualizarContadorSeleccionados();
    });

    checkboxGeneral.addEventListener('change', function() {
        document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox').forEach(cb => cb.checked = this.checked);
        actualizarContadorSeleccionados();
    });

    cuerpoTabla.addEventListener('change', function(event) {
        if (event.target.classList.contains('empleado-checkbox')) {
            const checkboxesVisibles = document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox');
            const seleccionadosVisibles = document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox:checked');
            checkboxGeneral.checked = checkboxesVisibles.length > 0 && checkboxesVisibles.length === seleccionadosVisibles.length;
            actualizarContadorSeleccionados();
        }
    });

    btnDesactivarSeleccionados.addEventListener('click', async function() {
        const checkboxesSeleccionados = Array.from(document.querySelectorAll('#cuerpoTablaActivos .empleado-checkbox:checked'));
        const idsParaDesactivar = checkboxesSeleccionados.map(cb => cb.value);

        if (idsParaDesactivar.length === 0) {
            agregarMensajeEstado('Por favor, selecciona al menos un empleado para desactivar.', 'warning');
            return;
        }

        if (!confirm(`¿Estás seguro de que quieres DESACTIVAR a ${idsParaDesactivar.length} empleado(s) seleccionado(s)?`)) {
            return;
        }

        this.disabled = true;
        this.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Desactivando (${idsParaDesactivar.length})...`;
        limpiarMensajes();

        let desactivadosConExito = 0;
        let erroresDesactivacion = 0;
        const promesas = [];

        agregarMensajeEstado(`Iniciando proceso de desactivación para ${idsParaDesactivar.length} empleado(s)...`, 'info', true);

        for (const id of idsParaDesactivar) {
            const empleadoData = todosLosEmpleadosCargados.find(emp => emp.id === id);
            const nombreEmpleado = empleadoData ? `${empleadoData.nombre || ''} ${empleadoData.apellido || ''}`.trim() : `ID ${id}`;

            const promesa = fetch(`/adminapp/empleados/${id}/desactivar`, { // ENDPOINT PARA DESACTIVAR
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // CSRF token si es necesario
                }
            })
                .then(async response => {
                    const responseText = await response.text();
                    if (response.ok) {
                        agregarMensajeEstado(`OK: ${nombreEmpleado} desactivado. (${responseText || 'Éxito'})`, 'success');
                        desactivadosConExito++;
                    } else {
                        agregarMensajeEstado(`Error: ${nombreEmpleado}. ${responseText || response.statusText}`, 'danger');
                        erroresDesactivacion++;
                    }
                })
                .catch(error => {
                    console.error(`Error en fetch para empleado ID ${id}:`, error);
                    agregarMensajeEstado(`Error de Red: ${nombreEmpleado}. ${error.message}`, 'danger');
                    erroresDesactivacion++;
                });
            promesas.push(promesa);
        }

        await Promise.allSettled(promesas);

        this.disabled = false;
        this.innerHTML = '<i class="bi bi-person-x-fill"></i> Desactivar Seleccionados';

        let mensajeFinal = `Proceso de desactivación completado. Desactivados: ${desactivadosConExito}. Errores: ${erroresDesactivacion}.`;
        agregarMensajeEstado(mensajeFinal, erroresDesactivacion > 0 ? 'warning' : 'info', true);

        if (desactivadosConExito > 0) {
            agregarMensajeEstado("Actualizando lista de empleados activos...", "info", true);
            setTimeout(() => {
                cargarEmpleadosActivos();
            }, 2000);
        }
        checkboxGeneral.checked = false;
        actualizarContadorSeleccionados();
    });

    // Carga inicial
    cargarEmpleadosActivos();
});