// Variables para mantener estado de la búsqueda
let paginaActual = 0;
let tamañoPagina = 10;
let totalPaginas = 0;
let totalElementos = 0;
let ordenarPor = "nombre";
let direccionOrden = "asc";

// Event listeners para los botones principales
document.getElementById('cargarDatos').addEventListener('click', () => obtenerEmpleados(0));
document.getElementById('aplicarFiltros').addEventListener('click', () => obtenerEmpleados(0));
document.getElementById('limpiarFiltros').addEventListener('click', limpiarFiltros);
document.addEventListener('DOMContentLoaded', () => obtenerEmpleados(0));

// Event listeners para ordenación
document.getElementById('ordenarPorNombre').addEventListener('click', () => {
    ordenarPor = "nombre";
    document.getElementById('ordenarPorNombre').classList.add('active');
    document.getElementById('ordenarPorSalario').classList.remove('active');
    obtenerEmpleados(0);
});

document.getElementById('ordenarPorSalario').addEventListener('click', () => {
    ordenarPor = "salario";
    document.getElementById('ordenarPorSalario').classList.add('active');
    document.getElementById('ordenarPorNombre').classList.remove('active');
    obtenerEmpleados(0);
});

document.getElementById('direccionOrden').addEventListener('click', () => {
    if (direccionOrden === "asc") {
        direccionOrden = "desc";
        document.getElementById('direccionOrden').textContent = "↓";
    } else {
        direccionOrden = "asc";
        document.getElementById('direccionOrden').textContent = "↑";
    }
    obtenerEmpleados(0);
});

// Función principal para obtener empleados con filtros, paginación y ordenación
function obtenerEmpleados(pagina) {
    const nombre = document.getElementById('filtroNombre').value.trim();
    const departamento = document.getElementById('filtroDepartamento').value.trim();
    const comentario = document.getElementById('filtroComentario').value.trim();
    const fechaLimite = document.getElementById('filtroFechaAntes').value;
    const salarioMinimo = document.getElementById('filtroSalarioMinimo').value;
    const salarioMaximo = document.getElementById('filtroSalarioMaximo').value;

    // Actualizar página actual
    paginaActual = pagina;

    // Construir URL con todos los parámetros
    let url = new URL('http://localhost:9090/empleados/listado');

    // Parámetros de filtro
    if (nombre) url.searchParams.append('nombre', nombre);
    if (departamento) url.searchParams.append('departamento', departamento);
    if (comentario) url.searchParams.append('comentario', comentario);
    if (fechaLimite) url.searchParams.append('contratadosAntesDe', fechaLimite);
    if (salarioMinimo) url.searchParams.append('salarioMinimo', salarioMinimo);
    if (salarioMaximo) url.searchParams.append('salarioMaximo', salarioMaximo);

    // Parámetros de paginación y ordenación
    url.searchParams.append('page', pagina);
    url.searchParams.append('size', tamañoPagina);
    url.searchParams.append('sortBy', ordenarPor);
    url.searchParams.append('sortDir', direccionOrden);

    // Mostrar indicador de carga
    document.getElementById('cargando').style.display = 'block';
    document.getElementById('tablaEmpleados').style.display = 'none';
    document.getElementById('error').style.display = 'none';
    document.getElementById('contadorResultados').textContent = '';

    fetch(url)
        .then(respuesta => {
            if (!respuesta.ok) {
                throw new Error('Error de red: ' + respuesta.status);
            }
            return respuesta.json();
        })
        .then(datos => {
            // Guardar información de paginación
            paginaActual = datos.number;
            totalPaginas = datos.totalPages;
            totalElementos = datos.totalElements;

            // Mostrar resultados
            llenarTabla(datos.content);
            // Crear controles de paginación
            crearControlesPaginacion();
            // Asignar event listeners a los botones
            asignarEventListenersAcciones();
            // Mostrar conteo de resultados
            document.getElementById('contadorResultados').textContent =
                `Mostrando ${datos.numberOfElements} de ${totalElementos} resultados - Página ${paginaActual + 1} de ${totalPaginas || 1}`;
        })
        .catch(error => {
            mostrarError("Error al obtener datos: " + error.message);
        });
}

// Función para limpiar filtros
function limpiarFiltros() {
    document.getElementById('filtroNombre').value = "";
    document.getElementById('filtroDepartamento').value = "";
    document.getElementById('filtroComentario').value = "";
    document.getElementById('filtroFechaAntes').value = "";
    document.getElementById('filtroSalarioMinimo').value = "";
    document.getElementById('filtroSalarioMaximo').value = "";
    obtenerEmpleados(0); // Resetear a primera página
}

// Función para crear controles de paginación
function crearControlesPaginacion() {
    const divPaginacion = document.getElementById('paginacion');
    divPaginacion.innerHTML = '';

    if (totalPaginas <= 1) return; // No mostrar paginación si hay una página o menos

    // Botón anterior
    const botonAnterior = document.createElement('button');
    botonAnterior.innerHTML = '&laquo; Anterior';
    botonAnterior.className = 'btn btn-outline-primary me-2';
    botonAnterior.disabled = paginaActual === 0;
    botonAnterior.addEventListener('click', () => obtenerEmpleados(paginaActual - 1));
    divPaginacion.appendChild(botonAnterior);

    // Botones de páginas
    const paginaInicio = Math.max(0, paginaActual - 2);
    const paginaFin = Math.min(totalPaginas - 1, paginaActual + 2);

    for (let i = paginaInicio; i <= paginaFin; i++) {
        const botonPagina = document.createElement('button');
        botonPagina.textContent = i + 1;
        botonPagina.className = i === paginaActual ? 'btn btn-primary me-2' : 'btn btn-outline-primary me-2';
        botonPagina.addEventListener('click', () => obtenerEmpleados(i));
        divPaginacion.appendChild(botonPagina);
    }

    // Botón siguiente
    const botonSiguiente = document.createElement('button');
    botonSiguiente.innerHTML = 'Siguiente &raquo;';
    botonSiguiente.className = 'btn btn-outline-primary';
    botonSiguiente.disabled = paginaActual >= totalPaginas - 1;
    botonSiguiente.addEventListener('click', () => obtenerEmpleados(paginaActual + 1));
    divPaginacion.appendChild(botonSiguiente);
}

// Función para llenar la tabla con los datos
function llenarTabla(datos) {
    const cuerpoTabla = document.getElementById('cuerpoTabla');
    cuerpoTabla.innerHTML = '';

    if (datos.length === 0) {
        // Mostrar mensaje si no hay resultados
        const fila = document.createElement('tr');
        const celda = document.createElement('td');
        celda.setAttribute('colspan', '11');
        celda.textContent = 'No se encontraron empleados con esos criterios';
        celda.style.textAlign = 'center';
        celda.style.padding = '20px';
        fila.appendChild(celda);
        cuerpoTabla.appendChild(fila);
    } else {
        // Mostrar empleados
        datos.forEach(emp => {
            const fila = document.createElement('tr');

            // Crear enlace en el nombre
            const nombreConEnlace = emp.nombre ?
                `<a href="/empleado/detalle/${emp.id}" class="employee-link">${emp.nombre}</a>` : 'N/A';

            // Mostrar jefe si existe
            const infoJefe = emp.nombreJefe ?
                `<a href="/empleado/detalle/${emp.idJefe}" class="employee-link">${emp.nombreJefe}</a>` :
                'N/A';

            // Mostrar especialidades
            const especialidades = formatearEspecialidades(emp.especialidades);

            //
            const usuarioBloqueado= emp.usuario.motivoBloqueo!=null;

            let botonesAccionLoginHTML = '';
            if (usuarioBloqueado) {
                // Usuario BLOQUEADO: Mostrar botón para DESBLOQUEAR
                botonesAccionLoginHTML = `
                    <a href="#" class="btn btn-success btn-desbloquear-js" data-employee-id="${emp.id}" title="Desbloquear Empleado">
                        <i class="bi bi-unlock-fill me-1"></i>
                    </a>
                `;
            } else {
                // Usuario NO BLOQUEADO: Mostrar botón para BLOQUEAR (redirige a página de motivos)
                botonesAccionLoginHTML = `
                    <a id="btnBloquear" href="/empleado/${emp.id}/bloquear/motivos" class="btn btn-warning " title="bloquear Empleado">
                        <i class="bi bi-lock-fill me-1"></i> 
                    </a>
                `;
            }
            fila.innerHTML = `
                        <td>${nombreConEnlace}</td>
                        <td>${emp.comentarios || 'N/A'}</td>
                        <td>${formatearFecha(emp.periodo?.fechaInicio)}</td>
                        <td>${formatearMoneda(emp.salario)}</td>
                        <td>${formatearMoneda(emp.comision)}</td>
                        <td>${formatearDepartamento(emp.departamento)}</td>
                        <td>${infoJefe}</td>
                        <td>${especialidades}</td>
                        <td><a href="/empleado/detalle/${emp.id}" class="btn btn-sm btn-primary"title="Detalle Empleado"><i class="bi bi-eye"></i></a></td>
                        <td>
                            <div> 
                                <a id="btnEditar" href="/empleado/modificar/${emp.id}" class="btn btn-success me-2" title="Editar Empleado">
                                    <i class="bi bi-pencil me-1"></i>
                                </a>
                                <a href="#" class="btn btn-danger me-2 btn-eliminar-empleado-js" data-employee-id="${emp.id}" data-employee-name="${emp.nombre}" title="Desactivar Empleado">
                                    <i class="bi bi-person-x"></i>
                                </a>
                                ${botonesAccionLoginHTML}
                            </div>
                            
                        </td>
                    `;
            cuerpoTabla.appendChild(fila);
        });
    }

    document.getElementById('cargando').style.display = 'none';
    document.getElementById('tablaEmpleados').style.display = 'table';
}

// Función para formatear especialidades
function formatearEspecialidades(especialidades) {
    if (!especialidades || especialidades.length === 0) return 'N/A';

    return especialidades.map(esp =>
        `<span class="especialidad-badge">${esp.nombre}</span>`
    ).join(' ');
}

// Funciones de formateo
function formatearFecha(textoFecha) {
    if (!textoFecha) return 'N/A';
    const fecha = new Date(textoFecha);
    return fecha.toLocaleDateString('es-ES');
}

function formatearMoneda(cantidad) {
    if (cantidad === undefined || cantidad === null) return 'N/A';
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR'
    }).format(cantidad);
}

function formatearDepartamento(departamento) {
    if (!departamento) return 'N/A';
    return `
                <div>
                    <a href="/departamento/detalle/${departamento.id}" class="employee-link">${departamento.nombre || '-'}</a>
                </div>
            `;
}

function mostrarError(mensaje) {
    const elementoError = document.getElementById('error');
    elementoError.textContent = mensaje;
    elementoError.style.display = 'block';
    document.getElementById('cargando').style.display = 'none';
}
function asignarEventListenersAcciones() {

    // --- Listener para botones de Desbloquear ---
    document.querySelectorAll('.btn-desbloquear-js').forEach(button => {
        // Clonar y reemplazar para evitar listeners duplicados si esta función se llama múltiples veces
        const clone = button.cloneNode(true);
        button.parentNode.replaceChild(clone, button);

        clone.addEventListener('click', (event) => {
            event.preventDefault(); // Evitar comportamiento por defecto del enlace '#'
            const employeeId = clone.getAttribute('data-employee-id');
            if (!employeeId) {
                console.error("No se encontró el ID del empleado en el botón.");
                return;
            }

            // Obtener nombre para confirmación
            const employeeRow = clone.closest('tr');
            const employeeName = employeeRow?.cells[1]?.textContent || employeeId; // Celda 1 (nombre) o ID

            if (confirm(`¿Está seguro de desbloquear al empleado ${employeeName}?`)) {
                // URL del endpoint (¡Asegúrate que sea la correcta! Probablemente /empleados/...)
                const url = `/empleados/${employeeId}/desbloquear`;

                // --- Cabeceras (si usas CSRF) ---
                const headers = {

                };


                // Deshabilitar botón temporalmente
                clone.classList.add('disabled');
                const originalText = clone.innerHTML;
                clone.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';


                // --- Llamada Fetch ---
                fetch(url, {
                    method: 'POST',
                    headers: headers // Añadir cabeceras si es necesario
                })
                    .then(response => {
                        if (response.ok) { // Status 200-299 OK
                            return null; // Éxito
                        } else {
                            // Error: Leer cuerpo para mensaje
                            return response.text().then(text => {
                                const error = new Error(text || `Error del servidor: ${response.status}`);
                                error.status = response.status;
                                throw error;
                            });
                        }
                    })
                    .then(() => {
                        // --- ÉXITO ---
                        console.log('Empleado desbloqueado con éxito');
                        alert('Empleado desbloqueado.');
                        // Cambiar botón visualmente (opcional)
                        // clone.outerHTML = '<a id="btnBloquear" href="/empleado/${emp.id}/bloquear/motivos" class="btn btn-primary me-2" title="bloquear Empleado">\n' +
                        //     '                                <i class="bi bi-lock-fill me-1"></i> \n' +
                        //     '                                </a>';
                        // clone.classList.remove('btn-success');
                        // clone.classList.add('btn-secondary', 'disabled'); // Ya está 'disabled' por la clase
                        // // NO RECARGAR: //
                        obtenerEmpleados(paginaActual);
                    })
                    .catch(error => {
                        // --- ERROR ---
                        console.error('Error al desbloquear:', error);
                        alert(`Error al desbloquear: ${error.message}`);
                        // Rehabilitar botón en caso de error
                        clone.classList.remove('disabled');
                        clone.innerHTML = originalText;
                    });
            }
        });
    });

    document.querySelectorAll('.btn-eliminar-empleado-js').forEach(button => {
        // Clonar y reemplazar para evitar listeners duplicados
        const clone = button.cloneNode(true);
        button.parentNode.replaceChild(clone, button);

        clone.addEventListener('click', (event) => {
            event.preventDefault(); // Evitar comportamiento por defecto del enlace '#'
            const employeeId = clone.getAttribute('data-employee-id');
            const employeeName = clone.getAttribute('data-employee-name') || 'este empleado'; // Nombre para el mensaje

            if (!employeeId) {
                console.error("No se encontró el ID del empleado en el botón de desactivar.");
                alert("Error: No se pudo obtener el ID del empleado.");
                return;
            }

            if (confirm(`¿Está seguro de que desea desactivar al empleado ${employeeName}? Esta acción cambiará su estado a inactivo.`)) {
                // URL del endpoint para desactivar
                const url = `/empleados/${employeeId}/desactivar`; // Ajusta si usaste PostMapping o el servicio

                // --- Cabeceras (si usas CSRF, necesitarías el token) ---


                // Deshabilitar botón temporalmente y mostrar spinner
                clone.classList.add('disabled');
                const originalIconHTML = clone.innerHTML; // Guardar el ícono original
                clone.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Desactivando...';


                fetch(url, {
                    method: 'POST', // o 'POST' si así lo definiste en el backend
                })
                    .then(response => {
                        if (response.ok) {
                            return response.text().then(text => text || "Empleado desactivado con éxito"); // O response.json() si devuelve un DTO
                        } else {
                            // Intentar leer el cuerpo del error para un mensaje más específico
                            return response.text().then(text => {
                                const errorMsg = text || `Error del servidor: ${response.status}`;
                                throw new Error(errorMsg);
                            });
                        }
                    })
                    .then(successMessage => {
                        // --- ÉXITO ---
                        console.log(successMessage);
                        alert(successMessage); // O un mensaje más amigable: `Empleado ${employeeName} desactivado correctamente.`

                        // Actualizar la UI:
                        // 1. Recargar los datos de la página actual para reflejar el cambio.
                        //    Esto es lo más simple si tu vista por defecto ya filtra por activos.
                        obtenerEmpleados(paginaActual);

                        // Alternativa: Si no quieres recargar toda la tabla, puedes intentar
                        // modificar la fila directamente o eliminarla si el filtro actual es "solo activos".
                        // Por ejemplo, si estás mostrando "Solo Activos", la fila desaparecerá.
                        // const filaEmpleado = clone.closest('tr');
                        // if (filaEmpleado && document.getElementById('filtroActivo').value === "true") {
                        //     filaEmpleado.remove();
                        //     // Actualizar contador de resultados si es necesario
                        // } else if (filaEmpleado) {
                        //     // Cambiar visualmente el estado en la fila (más complejo)
                        //     // Por ejemplo, podrías añadir un badge "Inactivo"
                        // }
                    })
                    .catch(error => {
                        // --- ERROR ---
                        console.error('Error al desactivar empleado:', error);
                        alert(`Error al desactivar empleado: ${error.message}`);
                        // Rehabilitar botón en caso de error
                        clone.classList.remove('disabled');
                        clone.innerHTML = originalIconHTML; // Restaurar el ícono original
                    });
            }
        });
    });


}