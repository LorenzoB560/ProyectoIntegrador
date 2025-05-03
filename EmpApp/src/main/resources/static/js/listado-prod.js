// --- Variables Globales para el Listado de Productos ---
let paginaActualProd = 0;
let tamanioPaginaProd = 10; // Número de productos por página
let totalPaginasProd = 0;
let totalElementosProd = 0;
let ordenarPorProd = "nombre"; // Campo por defecto para ordenar
let direccionOrdenProd = "asc"; // Dirección por defecto

// --- Event Listeners Iniciales ---
document.addEventListener('DOMContentLoaded', () => {
    // Cargar productos al iniciar la página
    obtenerProductos(0);

    // Asignar listeners a los botones de filtro si existen
    document.getElementById('aplicarFiltrosProd')?.addEventListener('click', () => obtenerProductos(0));
    document.getElementById('limpiarFiltrosProd')?.addEventListener('click', limpiarFiltrosProductos);

    // TODO: Asignar listeners a los botones de ordenación si los implementas
    // Ejemplo:
    // document.getElementById('ordenarProdPorNombre')?.addEventListener('click', () => { ... });
    // document.getElementById('ordenarProdPorPrecio')?.addEventListener('click', () => { ... });
    // document.getElementById('direccionOrdenProd')?.addEventListener('click', () => { ... });
});

// --- Función Principal para Obtener Productos ---
function obtenerProductos(pagina) {
    if (pagina < 0) return; // Evitar páginas negativas
    paginaActualProd = pagina;

    // Leer valores de los filtros
    const nombre = document.getElementById('filtroNombreProd')?.value.trim() || '';
    const tipo = document.getElementById('filtroTipoProd')?.value || ''; // El valor del select ('', 'Libro', 'Electrónico', etc.)
    const precio = document.getElementById('filtroPrecioProd')?.value || '';

    // Construir URL Relativa al endpoint REST de EmpApp
    let url = new URL('/productos/listado', window.location.origin);

    // Añadir parámetros de filtro a la URL si tienen valor
    if (nombre) url.searchParams.append('nombre', nombre);
    if (tipo) url.searchParams.append('tipo', tipo);
    if (precio) url.searchParams.append('precio', precio);

    // Añadir parámetros de paginación y ordenación
    url.searchParams.append('page', paginaActualProd);
    url.searchParams.append('size', tamanioPaginaProd);
    url.searchParams.append('sortBy', ordenarPorProd);
    url.searchParams.append('sortDir', direccionOrdenProd);

    // --- Referencias a elementos UI ---
    const cargandoDiv = document.getElementById('cargandoProd');
    const tablaProductos = document.getElementById('tablaProductos');
    const errorDiv = document.getElementById('errorProd');
    const contadorSpan = document.getElementById('contadorResultadosProd');
    const paginacionDiv = document.getElementById('paginacionProd');
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');

    // Mostrar carga, ocultar tabla/error/paginación
    if (cargandoDiv) cargandoDiv.style.display = 'block';
    if (tablaProductos) tablaProductos.style.display = 'none';
    if (errorDiv) errorDiv.style.display = 'none';
    if (contadorSpan) contadorSpan.textContent = '';
    if (paginacionDiv) paginacionDiv.innerHTML = ''; // Limpiar paginación anterior
    if (cuerpoTabla) cuerpoTabla.innerHTML = ''; // Limpiar tabla anterior

    // --- Llamada Fetch ---
    fetch(url)
        .then(respuesta => {
            if (!respuesta.ok) {
                // Intentar obtener mensaje de error del cuerpo
                return respuesta.text().then(text => {
                    throw new Error(text || `Error de red: ${respuesta.status}`);
                });
            }
            return respuesta.json(); // Convertir a JSON si la respuesta es OK
        })
        .then(datos => { // datos es el objeto Page<ProductoDTO>
            // Actualizar variables globales de paginación
            paginaActualProd = datos.number;
            totalPaginasProd = datos.totalPages;
            totalElementosProd = datos.totalElements;

            // Llenar la tabla con los productos recibidos
            llenarTablaProductos(datos.content); // datos.content es la List<ProductoDTO>

            // Crear controles de paginación
            crearControlesPaginacionProductos();

            // Mostrar contador de resultados
            if (contadorSpan) {
                const inicio = datos.numberOfElements > 0 ? (paginaActualProd * tamanioPaginaProd) + 1 : 0;
                const fin = inicio + datos.numberOfElements -1;
                // Corrección para mostrar 0 si no hay elementos
                const fin_mostrar = (fin < inicio) ? inicio : fin;
                contadorSpan.textContent = `Mostrando ${inicio}-${fin_mostrar} de ${totalElementosProd} productos - Página ${paginaActualProd + 1} de ${totalPaginasProd || 1}`;
            }
            if (tablaProductos) tablaProductos.style.display = 'table'; // Mostrar tabla

        })
        .catch(error => {
            console.error("Error al obtener productos:", error);
            mostrarErrorProd(`Error al obtener datos: ${error.message}`);
        })
        .finally(() => {
            // Ocultar indicador de carga siempre
            if (cargandoDiv) cargandoDiv.style.display = 'none';
        });
}

// --- Función para Llenar la Tabla de Productos ---
function llenarTablaProductos(productos) {
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');
    const tabla = document.getElementById('tablaProductos');
    if (!cuerpoTabla || !tabla) return;

    cuerpoTabla.innerHTML = ''; // Limpiar

    if (!productos || productos.length === 0) {
        const numColumnas = tabla.querySelector('thead tr')?.cells.length || 6; // Ajusta al número de columnas
        cuerpoTabla.innerHTML = `<tr><td colspan="${numColumnas}" class="text-center p-3 fst-italic text-muted">No se encontraron productos con los criterios seleccionados.</td></tr>`;
    } else {
        productos.forEach(prod => {
            const fila = document.createElement('tr');
            // Acortar descripción para la tabla
            const descCorta = prod.descripcion ? (prod.descripcion.length > 60 ? prod.descripcion.substring(0, 60) + '...' : prod.descripcion) : 'N/A';
            // Formatear precio
            const precioFormateado = formatearMonedaProd(prod.precio);

            // Generar URL de detalle usando el ID
            const detalleUrl = `/producto/detalle/${prod.id}`; // Ajusta si la ruta base es diferente

            fila.innerHTML = `
                <td><small class="text-muted">${prod.id || 'N/A'}</small></td>
                <td>${prod.nombre || 'N/A'}</td>
                <td><span class="badge bg-info text-dark">${prod.tipoProductoNombre || 'N/A'}</span></td>
                <td>${precioFormateado}</td>
                <td>${descCorta}</td>
                <td>
                    <a href="${detalleUrl}" class="btn btn-sm btn-outline-primary" title="Ver Detalle">
                        <i class="bi bi-eye"></i> Ver
                    </a>
                    </td>
            `;
            cuerpoTabla.appendChild(fila);
        });
    }
}

// --- Función para Crear Paginación de Productos ---
function crearControlesPaginacionProductos() {
    const divPaginacion = document.getElementById('paginacionProd');
    if (!divPaginacion) return;
    divPaginacion.innerHTML = ''; // Limpiar

    if (totalPaginasProd <= 1) return; // No mostrar si solo hay 1 página

    const crearBoton = (texto, pagina, habilitado = true, activo = false) => {
        const li = document.createElement('li');
        li.className = `page-item ${!habilitado ? 'disabled' : ''} ${activo ? 'active' : ''}`;
        const a = document.createElement('a');
        a.className = 'page-link';
        a.href = '#'; // Evitar navegación real
        a.innerHTML = texto;
        if (habilitado && !activo) {
            a.addEventListener('click', (e) => {
                e.preventDefault(); // Prevenir el # en la URL
                obtenerProductos(pagina);
            });
        }
        // Añadir aria-current a la página activa para accesibilidad
        if (activo) {
            a.setAttribute('aria-current', 'page');
        }
        li.appendChild(a);
        return li;
    };

    const ul = document.createElement('ul');
    ul.className = 'pagination pagination-sm justify-content-center'; // Centrar y hacer pequeño

    // Botón Anterior
    ul.appendChild(crearBoton('&laquo; <span class="d-none d-sm-inline">Anterior</span>', paginaActualProd - 1, paginaActualProd > 0)); // Texto responsive

    // Lógica de números de página (ejemplo: mostrar 5 botones como máximo)
    const maxBotonesVisibles = 5;
    let inicio = Math.max(0, paginaActualProd - Math.floor(maxBotonesVisibles / 2));
    let fin = Math.min(totalPaginasProd - 1, inicio + maxBotonesVisibles - 1);
    if(fin === totalPaginasProd - 1){
        inicio = Math.max(0, fin - maxBotonesVisibles + 1);
    }

    if (inicio > 0) { // Botón Primera página si no se muestra
        ul.appendChild(crearBoton('1', 0));
        if (inicio > 1) { // Puntos suspensivos si hay hueco
            const liPuntos = document.createElement('li');
            liPuntos.className = 'page-item disabled';
            liPuntos.innerHTML = '<span class="page-link">...</span>';
            ul.appendChild(liPuntos);
        }
    }

    // Números de página centrales
    for (let i = inicio; i <= fin; i++) {
        ul.appendChild(crearBoton(i + 1, i, true, i === paginaActualProd));
    }

    if (fin < totalPaginasProd - 1) { // Puntos suspensivos si hay hueco
        if (fin < totalPaginasProd - 2) {
            const liPuntos = document.createElement('li');
            liPuntos.className = 'page-item disabled';
            liPuntos.innerHTML = '<span class="page-link">...</span>';
            ul.appendChild(liPuntos);
        }
        // Botón Última página si no se muestra
        ul.appendChild(crearBoton(totalPaginasProd, totalPaginasProd - 1));
    }

    // Botón Siguiente
    ul.appendChild(crearBoton('<span class="d-none d-sm-inline">Siguiente</span> &raquo;', paginaActualProd + 1, paginaActualProd < totalPaginasProd - 1)); // Texto responsive

    divPaginacion.appendChild(ul);
}

// --- Función para Limpiar Filtros de Productos ---
function limpiarFiltrosProductos() {
    const nombreInput = document.getElementById('filtroNombreProd');
    const tipoSelect = document.getElementById('filtroTipoProd');
    const precioInput = document.getElementById('filtroPrecioProd');

    if(nombreInput) nombreInput.value = '';
    if(tipoSelect) tipoSelect.value = ''; // Resetear a 'Todos'
    if(precioInput) precioInput.value = '';

    obtenerProductos(0); // Recargar desde la primera página
}

// --- Función para Mostrar Errores ---
function mostrarErrorProd(mensaje) {
    const errorDiv = document.getElementById('errorProd');
    const tablaProductos = document.getElementById('tablaProductos');
    const paginacionDiv = document.getElementById('paginacionProd');

    if (errorDiv) {
        errorDiv.textContent = mensaje;
        errorDiv.style.display = 'block';
    }
    if (tablaProductos) tablaProductos.style.display = 'none';
    if (paginacionDiv) paginacionDiv.innerHTML = '';
}

// --- Función Auxiliar Formato Moneda ---
function formatearMonedaProd(cantidad) {
    if (cantidad === undefined || cantidad === null || isNaN(cantidad)) return 'N/A';
    try {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR',
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(cantidad);
    } catch (e) {
        console.warn("Error formateando moneda:", cantidad, e);
        return 'N/A';
    }
}