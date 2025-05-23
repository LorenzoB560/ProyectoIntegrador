// --- Variables Globales ---
let paginaActualProd = 0;
const tamañoPaginaProd = 10; // Tamaño de página por defecto
let ordenarPorProd = "descripcion"; // Campo de ordenación por defecto ('descripcion' enviado a la API)
let direccionOrdenProd = "asc"; // Dirección de ordenación por defecto
let totalPaginasProd = 0;
let totalElementosProd = 0;

// --- URL del Endpoint API ---
const API_PRODUCTOS_URL = '/productos/listado';

// --- Event Listeners y Setup Inicial ---
document.addEventListener('DOMContentLoaded', () => {
    $('#filtroCategoriasProd').select2({
        theme: "bootstrap-5", width: 'style', placeholder: "Seleccionar categorías...",
        closeOnSelect: false, allowClear: true
    });

    document.getElementById('aplicarFiltrosProd')?.addEventListener('click', () => obtenerProductos(0));
    document.getElementById('limpiarFiltrosProd')?.addEventListener('click', limpiarFiltrosProductos);

    // IDs de botones de ordenación deben coincidir con el HTML
    document.getElementById('ordenarPorDescripcionProd')?.addEventListener('click', () => cambiarOrdenacionProductos("descripcion"));
    document.getElementById('ordenarPorCategoriaProd')?.addEventListener('click', () => cambiarOrdenacionProductos("categoriaPrincipal"));
    document.getElementById('ordenarPorPrecioProd')?.addEventListener('click', () => cambiarOrdenacionProductos("precioBase"));


    document.getElementById('direccionOrdenProd')?.addEventListener('click', () => {
        direccionOrdenProd = direccionOrdenProd === "asc" ? "desc" : "asc";
        actualizarIconoDireccionOrdenProductos();
        obtenerProductos(paginaActualProd);
    });

    actualizarIconoDireccionOrdenProductos();
    actualizarBotonActivoProductos();
    obtenerProductos(0); // Carga inicial
});

// --- Funciones de Ordenación ---
function cambiarOrdenacionProductos(nuevoOrdenarPor) {
    if (ordenarPorProd !== nuevoOrdenarPor) {
        ordenarPorProd = nuevoOrdenarPor;
        direccionOrdenProd = "asc";
    } else {
        direccionOrdenProd = direccionOrdenProd === "asc" ? "desc" : "asc";
    }
    actualizarBotonActivoProductos();
    actualizarIconoDireccionOrdenProductos();
    obtenerProductos(0);
}

function actualizarBotonActivoProductos() {
    document.querySelectorAll('.btn-sort').forEach(btn => btn.classList.remove('active'));
    let btnActivoId = '';
    switch (ordenarPorProd.toLowerCase()) {
        case 'descripcion':
            btnActivoId = 'ordenarPorDescripcionProd';
            break;
        case 'categoriaprincipal': // Valor que maneja el JS para categoría
            btnActivoId = 'ordenarPorCategoriaProd';
            break;
        case 'preciobase': // O 'precio', según lo que uses
            btnActivoId = 'ordenarPorPrecioProd';
            break;
        // ELIMINA O COMENTA EL SIGUIENTE CASO:
        default:
            if (!btnActivoId && ordenarPorProd !== "descripcion") {
                // No hacer nada o default a descripción si es necesario
            } else if (!btnActivoId) { // Si no hay match y es el default inicial
                document.getElementById('ordenarPorDescripcionProd')?.classList.add('active');
            }
            break;
    }
    if (btnActivoId) {
        document.getElementById(btnActivoId)?.classList.add('active');
    } else if (ordenarPorProd === "descripcion") { // Asegurar que el default visual se aplique
        document.getElementById('ordenarPorDescripcionProd')?.classList.add('active');
    }
}

function actualizarIconoDireccionOrdenProductos() {
    const btnDireccion = document.getElementById('direccionOrdenProd');
    if (btnDireccion) {
        btnDireccion.innerHTML = direccionOrdenProd === "asc" ? "↑" : "↓";
    }
}

// --- Función Principal Fetch ---
function obtenerProductos(pagina) {
    paginaActualProd = pagina;

    // Recoger filtros
    const descripcionPattern = document.getElementById('filtroDescripcionProd')?.value.trim() || "";
    const proveedorId = document.getElementById('filtroProveedorProd')?.value || "";
    const idsCategorias = $('#filtroCategoriasProd').val();
    const precioMin = document.getElementById('filtroPrecioMinProd')?.value || "";
    const precioMax = document.getElementById('filtroPrecioMaxProd')?.value || "";
    let esSegundaManoValue = "";
    // Asegúrate que el name en HTML sea "filtroEsSegundaManoProd"
    const esSegundaManoRadios = document.getElementsByName('filtroEsSegundaManoProd');
    for (const radio of esSegundaManoRadios) {
        if (radio.checked) { esSegundaManoValue = radio.value; break; }
    }

    // Construir URL
    const url = new URL(API_PRODUCTOS_URL, window.location.origin);
    if (descripcionPattern) url.searchParams.append('descripcionPattern', descripcionPattern);
    if (proveedorId) url.searchParams.append('proveedorId', proveedorId);
    if (idsCategorias && idsCategorias.length > 0) {
        idsCategorias.forEach(id => url.searchParams.append('idsCategorias', id));
    }
    if (esSegundaManoValue !== "") url.searchParams.append('esSegundaMano', esSegundaManoValue);
    if (precioMin) url.searchParams.append('precioMin', precioMin);
    if (precioMax) url.searchParams.append('precioMax', precioMax);

    url.searchParams.append('page', paginaActualProd);
    url.searchParams.append('size', tamañoPaginaProd);
    url.searchParams.append('sortBy', ordenarPorProd); // Enviar 'descripcion' o 'categoriaPrincipal'
    url.searchParams.append('sortDir', direccionOrdenProd);

    // UI de Carga
    document.getElementById('cargandoProd').style.display = 'block';
    document.getElementById('tablaProductos').style.display = 'none';
    document.getElementById('mensajeTablaVaciaProd').style.display = 'none';
    document.getElementById('errorProd').style.display = 'none';
    document.getElementById('contadorResultadosProd').textContent = '';

    // Fetch
    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.json().catch(() => null).then(errorBody => {
                    const errorMessage = errorBody?.message || errorBody?.error || `Error: ${response.status}`;
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(data => {
            paginaActualProd = data.number;
            totalPaginasProd = data.totalPages;
            totalElementosProd = data.totalElements;
            llenarTablaProductos(data.content); // Llenar tabla
            crearControlesPaginacionProductos(); // Crear paginación
            document.getElementById('contadorResultadosProd').textContent =
                `Mostrando ${data.numberOfElements} de ${totalElementosProd} productos - Página ${paginaActualProd + 1} de ${totalPaginasProd || 1}`;
        })
        .catch(error => {
            console.error("Error detallado:", error);
            mostrarErrorProductos("Error al obtener productos: " + error.message);
        })
        .finally(() => {
            document.getElementById('cargandoProd').style.display = 'none';
            if (totalElementosProd > 0) {
                document.getElementById('tablaProductos').style.display = 'table';
            } else if (document.getElementById('errorProd').style.display !== 'block') {
                document.getElementById('mensajeTablaVaciaProd').style.display = 'block';
            }
        });
}

// --- Limpiar Filtros ---
function limpiarFiltrosProductos() {
    document.getElementById('filtroDescripcionProd').value = "";
    document.getElementById('filtroProveedorProd').value = "";
    $('#filtroCategoriasProd').val(null).trigger('change');
    document.getElementById('segundaManoTodosProd').checked = true; // Asume ID del radio "Todos"
    document.getElementById('filtroPrecioMinProd').value = "";
    document.getElementById('filtroPrecioMaxProd').value = "";
    ordenarPorProd = "descripcion"; // Resetear orden
    direccionOrdenProd = "asc";
    actualizarBotonActivoProductos();
    actualizarIconoDireccionOrdenProductos();
    obtenerProductos(0);
}

// --- Llenar Tabla (Estilo innerHTML) ---
function llenarTablaProductos(productos) {
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');
    cuerpoTabla.innerHTML = ''; // Limpiar contenido previo

    if (!productos || productos.length === 0) {
        // El mensaje de tabla vacía se gestiona en la lógica de .finally() de la llamada fetch
        return;
    }

    productos.forEach(prod => {
        const fila = document.createElement('tr');

        // Preparar datos del ProductoDTO
        const productoId = prod.id || 'N/A';
        const productoDescripcion = prod.descripcion || 'N/A';
        const precioFormateado = formatearMonedaProd(prod.precio); // Asumiendo que ya tienes esta función

        // Obtener nombre del proveedor (NUEVO)
        // Se accede a través de prod.proveedor (que es ProveedorDTO) y luego a su campo .nombre
        const nombreProveedor = (prod.proveedor && prod.proveedor.nombre) ? prod.proveedor.nombre : 'N/A';

        const marcaProducto = prod.marca || 'N/A';

        let categoriasHtml = 'N/A';
        if (prod.categoria && Array.isArray(prod.categoria) && prod.categoria.length > 0) {
            categoriasHtml = prod.categoria.map(cat => `<span class="badge bg-info text-dark me-1">${cat.nombre || ''}</span>`).join(' ');
        } else if (prod.categoria && typeof prod.categoria === 'object' && !Array.isArray(prod.categoria) && Object.keys(prod.categoria).length > 0) {
            // Manejo por si 'categoria' es un solo objeto en lugar de un array (aunque el DTO lo define como Set)
            categoriasHtml = `<span class="badge bg-info text-dark me-1">${prod.categoria.nombre || ''}</span>`;
        }

        let segundaManoHtml = '<i class="bi bi-question-circle text-muted"></i>';
        if (prod.segundaMano === true) segundaManoHtml = '<i class="bi bi-check-circle-fill text-success"></i> Sí';
        else if (prod.segundaMano === false) segundaManoHtml = '<i class="bi bi-x-circle-fill text-danger"></i> No';

        const unidadesProducto = prod.unidades ?? 'N/A'; // Usar '??' para manejar null o undefined

        const urlBaseAcciones = '/producto';
        const nombreEscapado = productoDescripcion.replace(/'/g, "\\'").replace(/"/g, "&quot;");

        const accionesHtml = `
            <div>
                <a href="${urlBaseAcciones}/detalle/${productoId}" class="btn btn-sm btn-primary me-2" title="Ver Detalle">
                    <i class="bi bi-eye"></i>
                </a>
            </div>`;

        // Construir la fila con la nueva celda para el proveedor
        fila.innerHTML = `
            <td>${productoDescripcion}</td>
            <td>${precioFormateado}</td>
            <td>${nombreProveedor}</td> 
            <td>${marcaProducto}</td>
            <td>${categoriasHtml}</td>
            <td>${segundaManoHtml}</td>
            <td>${unidadesProducto}</td>
            <td>${accionesHtml}</td>
        `;
        cuerpoTabla.appendChild(fila);
    });
}


// --- Crear Paginación ---
function crearControlesPaginacionProductos() {
    const divPaginacion = document.getElementById('paginacionProd');
    divPaginacion.innerHTML = '';
    if (totalPaginasProd <= 1) return;
    const ul = document.createElement('ul');
    ul.className = 'pagination pagination-sm justify-content-center';

    const maxBotones = 5;
    let inicio = Math.max(0, paginaActualProd - Math.floor(maxBotones / 2));
    let fin = Math.min(totalPaginasProd - 1, inicio + maxBotones - 1);
    if (fin - inicio + 1 < maxBotones) { // Ajustar si está cerca de los bordes
        if (inicio === 0) fin = Math.min(totalPaginasProd - 1, maxBotones - 1);
        else if (fin === totalPaginasProd - 1) inicio = Math.max(0, totalPaginasProd - maxBotones);
    }


    // Primera y Anterior
    ul.appendChild(crearBotonPaginacionProd('&laquo;', 0, paginaActualProd === 0, 'Primera página'));
    ul.appendChild(crearBotonPaginacionProd('&lsaquo;', paginaActualProd - 1, paginaActualProd === 0, 'Página anterior'));

    // Puntos iniciales
    if (inicio > 0) ul.appendChild(crearBotonPaginacionProd('...', -1, true)); // Botón deshabilitado

    // Números de página
    for (let i = inicio; i <= fin; i++) {
        ul.appendChild(crearBotonPaginacionProd(i + 1, i, false, `Página ${i + 1}`, i === paginaActualProd));
    }

    // Puntos finales
    if (fin < totalPaginasProd - 1) ul.appendChild(crearBotonPaginacionProd('...', -1, true));

    // Siguiente y Última
    ul.appendChild(crearBotonPaginacionProd('&rsaquo;', paginaActualProd + 1, paginaActualProd >= totalPaginasProd - 1, 'Página siguiente'));
    ul.appendChild(crearBotonPaginacionProd('&raquo;', totalPaginasProd - 1, paginaActualProd >= totalPaginasProd - 1, 'Última página'));

    divPaginacion.appendChild(ul);
}

// Helper para crear botones de paginación
function crearBotonPaginacionProd(texto, paginaDestino, deshabilitado, titulo = '', activo = false) {
    const li = document.createElement('li');
    li.className = `page-item ${deshabilitado ? 'disabled' : ''} ${activo ? 'active' : ''}`;
    const a = document.createElement('a');
    a.className = 'page-link';
    a.href = '#';
    a.innerHTML = texto;
    if (titulo) a.title = titulo;
    if (!deshabilitado && !activo && paginaDestino !== -1) {
        a.addEventListener('click', (e) => { e.preventDefault(); obtenerProductos(paginaDestino); });
    }
    li.appendChild(a);
    return li;
}


// --- Funciones Auxiliares ---
function formatearMonedaProd(cantidad) {
    if (cantidad === undefined || cantidad === null || isNaN(parseFloat(cantidad))) return 'N/A';
    try {
        return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(cantidad);
    } catch (e) { return 'N/A'; }
}

function mostrarErrorProductos(mensaje) {
    const elementoError = document.getElementById('errorProd');
    if (elementoError) {
        elementoError.textContent = mensaje;
        elementoError.style.display = 'block';
    }
    // Ocultar otros elementos al mostrar error
    document.getElementById('cargandoProd').style.display = 'none';
    document.getElementById('tablaProductos').style.display = 'none';
    document.getElementById('mensajeTablaVaciaProd').style.display = 'none';
    document.getElementById('paginacionProd').innerHTML = ''; // Limpiar paginación
    document.getElementById('contadorResultadosProd').textContent = ''; // Limpiar contador
}

