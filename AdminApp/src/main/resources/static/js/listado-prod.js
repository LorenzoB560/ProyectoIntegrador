// Archivo: ProyectoIntegrador/AdminApp/src/main/resources/static/js/listado-prod.js

// --- Variables Globales ---
let paginaActualProd = 0;
let tamanioPaginaProd = 10; // Puedes ajustar
let totalPaginasProd = 0;
let totalElementosProd = 0;
let ordenarPorProd = "descripcion"; // Default según últimos requisitos
let direccionOrdenProd = "asc";

// --- Inicialización y Listeners ---
document.addEventListener('DOMContentLoaded', () => {
    // Inicializar Select2 para categorías (requiere jQuery)
    if (typeof $ !== 'undefined' && $.fn.select2) {
        $('#filtroCategoriasProd').select2({
            theme: "bootstrap-5",
            width: 'style',
            placeholder: "Seleccionar categorías...",
            closeOnSelect: false,
            allowClear: true
        });
    } else {
        console.warn("jQuery o Select2 no están cargados.");
    }

    obtenerProductos(0); // Carga inicial

    // Listeners Filtros
    document.getElementById('aplicarFiltrosProd')?.addEventListener('click', () => obtenerProductos(0));
    document.getElementById('limpiarFiltrosProd')?.addEventListener('click', limpiarFiltrosProductos);

    // Listeners Ordenación
    const btnOrdenarDescripcion = document.getElementById('ordenarProdPorDescripcion');
    const btnOrdenarPrecio = document.getElementById('ordenarProdPorPrecio');
    const btnDireccion = document.getElementById('direccionOrdenProd');
    const botonesOrdenar = [btnOrdenarDescripcion, btnOrdenarPrecio];

    const handleSortButtonClick = (button, sortByField) => {
        if (!button) return;
        button.addEventListener('click', () => {
            if (ordenarPorProd !== sortByField) {
                ordenarPorProd = sortByField;
                direccionOrdenProd = "asc";
                if (btnDireccion) btnDireccion.innerHTML = '↑';
                botonesOrdenar.forEach(btn => btn?.classList.remove('active'));
                button.classList.add('active');
                obtenerProductos(0);
            }
        });
    };
    handleSortButtonClick(btnOrdenarDescripcion, 'descripcion');
    handleSortButtonClick(btnOrdenarPrecio, 'precio');

    if (btnDireccion) {
        btnDireccion.addEventListener('click', () => {
            direccionOrdenProd = (direccionOrdenProd === "asc") ? "desc" : "asc";
            btnDireccion.innerHTML = (direccionOrdenProd === "asc") ? '↑' : '↓';
            obtenerProductos(0);
        });
    }

    // Cargar opciones para selects si no se hizo con Thymeleaf
    // cargarOpcionesSelect('/api/productos/proveedores', 'filtroProveedorProd', '-- Todos --', 'id', 'nombre');
    // cargarOpcionesSelect('/api/productos/categorias', 'filtroCategoriasProd', null, 'id', 'nombre'); // No aplica opción default a multiple
});

// --- Función Principal para Obtener Productos ---
function obtenerProductos(pagina) {
    if (pagina < 0) pagina = 0;
    paginaActualProd = pagina;

    // Leer filtros nuevos
    const descripcionPatron = document.getElementById('filtroDescripcionProd')?.value.trim() || '';
    const idProveedor = document.getElementById('filtroProveedorProd')?.value || '';
    const esPerecederoRadio = document.querySelector('input[name="filtroPerecederoProd"]:checked');
    const esPerecedero = esPerecederoRadio ? esPerecederoRadio.value : '';
    const precioMin = document.getElementById('filtroPrecioMinProd')?.value || '';
    const precioMax = document.getElementById('filtroPrecioMaxProd')?.value || '';
    const idsCategoriasSeleccionadas = $('#filtroCategoriasProd').val() || []; // Requiere jQuery

    // Construir URL API AdminApp
    let url = new URL('/api/productos/listado', window.location.origin);

    // Añadir parámetros (coinciden con ProductoSearchDTO)
    if (descripcionPatron) url.searchParams.append('descripcionPatron', descripcionPatron);
    if (idProveedor) url.searchParams.append('idProveedor', idProveedor);
    if (esPerecedero !== '') url.searchParams.append('esPerecedero', esPerecedero);
    if (precioMin) url.searchParams.append('precioMin', precioMin);
    if (precioMax) url.searchParams.append('precioMax', precioMax);
    idsCategoriasSeleccionadas.forEach(idCat => url.searchParams.append('idsCategorias', idCat));

    // Paginación y ordenación
    url.searchParams.append('page', paginaActualProd);
    url.searchParams.append('size', tamanioPaginaProd);
    url.searchParams.append('sortBy', ordenarPorProd);
    url.searchParams.append('sortDir', direccionOrdenProd);

    // --- Referencias UI y Fetch ---
    const cargandoDiv = document.getElementById('cargandoProd');
    const tablaProductos = document.getElementById('tablaProductos');
    const errorDiv = document.getElementById('errorProd');
    const contadorSpan = document.getElementById('contadorResultadosProd');
    const paginacionDiv = document.getElementById('paginacionProd');
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');
    const mensajeVacioDiv = document.getElementById('mensajeTablaVaciaProd');

    if (!cargandoDiv || !tablaProductos || !errorDiv || !contadorSpan || !paginacionDiv || !cuerpoTabla || !mensajeVacioDiv) {
        console.error("Faltan elementos UI esenciales."); return;
    }

    cargandoDiv.style.display = 'block'; tablaProductos.style.display = 'none'; errorDiv.style.display = 'none';
    mensajeVacioDiv.style.display = 'none'; contadorSpan.textContent = ''; paginacionDiv.innerHTML = '';
    cuerpoTabla.innerHTML = '';

    fetch(url)
        .then(respuesta => {
            if (!respuesta.ok) { return respuesta.text().then(text => { throw new Error(text || `Error ${respuesta.status}`); }); }
            return respuesta.json();
        })
        .then(datos => { // datos es Page<ProductoDTO>
            paginaActualProd = datos.number; totalPaginasProd = datos.totalPages; totalElementosProd = datos.totalElements;
            llenarTablaProductos(datos.content); // Llena tabla

            if (datos.content && datos.content.length > 0) {
                crearControlesPaginacionProductos(); // Crea paginación
                tablaProductos.style.display = 'table'; // Muestra tabla
                mensajeVacioDiv.style.display = 'none';
            } else {
                mensajeVacioDiv.style.display = 'block'; // Muestra mensaje vacío
                tablaProductos.style.display = 'none';
            }

            asignarEventListenersAccionesProd(); // Asigna listeners a botones

            const inicio = datos.numberOfElements > 0 ? (paginaActualProd * tamanioPaginaProd) + 1 : 0;
            const fin = inicio + datos.numberOfElements - 1;
            const fin_mostrar = (fin < inicio) ? inicio : fin;
            contadorSpan.textContent = `Mostrando ${inicio}-${fin_mostrar} de ${totalElementosProd} productos - Página ${paginaActualProd + 1} de ${totalPaginasProd || 1}`;
        })
        .catch(error => { console.error("Error:", error); mostrarErrorProd(`Error al obtener datos: ${error.message}`); })
        .finally(() => { cargandoDiv.style.display = 'none'; });
}

// --- Llenar Tabla (Adaptada a nuevos campos y columnas) ---
function llenarTablaProductos(productos) {
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');
    const tabla = document.getElementById('tablaProductos');
    if (!cuerpoTabla || !tabla) return;
    cuerpoTabla.innerHTML = '';

    if (!productos || productos.length === 0) { return; } // Mensaje vacío se controla fuera

    productos.forEach(prod => { // prod es ProductoDTO
        const fila = document.createElement('tr');
        const precioFormateado = formatearMonedaProdAdmin(prod.precio);
        const nombreProveedor = prod.proveedor ? prod.proveedor.nombre : 'N/D';
        const nombresCategorias = prod.categorias && prod.categorias.length > 0
            ? prod.categorias.map(cat => `<span class="badge bg-light text-dark border me-1">${cat.nombre || ''}</span>`).join(' ')
            : '<span class="text-muted small">N/A</span>';
        const esPerecederoTexto = prod.esPerecedero === true ? '<i class="bi bi-check-circle-fill text-success"></i>' :
            (prod.esPerecedero === false ? '<i class="bi bi-x-circle-fill text-danger"></i>' :
                '<span class="text-muted">N/D</span>');
        const descCorta = prod.descripcion ? (prod.descripcion.length > 80 ? prod.descripcion.substring(0, 80) + '...' : prod.descripcion) : 'N/A';
        const nombreProducto = prod.nombre || '(Sin Nombre)'; // Usar nombre del producto

        // HTML de la fila con las nuevas columnas
        fila.innerHTML = `
            <td>${nombreProducto}</td>
            <td><small>${nombreProveedor}</small></td>
            <td><small>${nombresCategorias}</small></td>
            <td class="text-end">${precioFormateado}</td>
            <td class="text-center">${prod.unidades !== null ? prod.unidades : 'N/D'}</td>
            <td class="text-center" title="${prod.esPerecedero === true ? 'Sí' : (prod.esPerecedero === false ? 'No' : '')}">${esPerecederoTexto}</td>
            <td><small>${descCorta}</small></td>
            <td class="text-end">
                <div class="d-flex flex-nowrap justify-content-end">
                    <button type="button" class="btn btn-sm btn-outline-danger btn-eliminar-prod-js"
                            data-product-id="${prod.id}"
                            title="Eliminar Producto ${prod.id}">
                        <i class="bi bi-trash"></i>
                    </button>
                    </div>
            </td>
        `;
        cuerpoTabla.appendChild(fila);
    });
}

// --- Asignar Listeners (Eliminar con ID en botón y sin nombre en confirm) ---
function asignarEventListenersAccionesProd() {
    document.querySelectorAll('.btn-eliminar-prod-js').forEach(button => {
        const clone = button.cloneNode(true);
        button.parentNode.replaceChild(clone, button);
        clone.addEventListener('click', (event) => {
            event.preventDefault();
            const productId = clone.dataset.productId; // Lee del botón
            if (!productId) { console.error("ID producto no encontrado"); alert("Error ID"); return; }
            // Confirmación genérica SIN NOMBRE
            if (confirm(`¿Está seguro de eliminar el producto con ID: ${productId}? Esta acción no se puede deshacer.`)) {
                const url = `/api/productos/${productId}`; // Endpoint DELETE AdminApp
                clone.disabled = true;
                const originalHtml = clone.innerHTML;
                clone.innerHTML = '<span class="spinner-border spinner-border-sm"></span>';
                fetch(url, { method: 'DELETE' /*, headers: headersCSRF */ })
                    .then(response => {
                        if (response.ok || response.status === 204) { return null; }
                        else { return response.text().then(text => { throw new Error(text || `Error ${response.status}`); }); }
                    })
                    .then(() => {
                        alert(`Producto con ID: ${productId} eliminado correctamente.`); // Mensaje sin nombre
                        obtenerProductos(paginaActualProd); // Recargar
                    })
                    .catch(error => {
                        alert(`Error al eliminar: ${error.message}`);
                        clone.disabled = false; clone.innerHTML = originalHtml;
                    });
            }
        });
    });
    // ... otros listeners ...
}

// --- Paginación ---
function crearControlesPaginacionProductos() {
    const divPaginacion = document.getElementById('paginacionProd'); // ID correcto
    if (!divPaginacion) return; divPaginacion.innerHTML = ''; if (totalPaginasProd <= 1) return;
    const crearBoton = (texto, pagina, habilitado = true, activo = false) => {
        const li = document.createElement('li'); li.className = `page-item ${!habilitado ? 'disabled' : ''} ${activo ? 'active' : ''}`; const a = document.createElement('a'); a.className = 'page-link'; a.href = '#'; a.innerHTML = texto; if (habilitado && !activo) { a.addEventListener('click', (e) => { e.preventDefault(); obtenerProductos(pagina); }); } if (activo) { a.setAttribute('aria-current', 'page'); } li.appendChild(a); return li;
    }; const ul = document.createElement('ul'); ul.className = 'pagination pagination-sm justify-content-center'; ul.appendChild(crearBoton('&laquo; <span class="d-none d-sm-inline">Anterior</span>', paginaActualProd - 1, paginaActualProd > 0)); const maxBotonesVisibles = 5; let inicio = Math.max(0, paginaActualProd - Math.floor(maxBotonesVisibles / 2)); let fin = Math.min(totalPaginasProd - 1, inicio + maxBotonesVisibles - 1); if(fin === totalPaginasProd - 1){ inicio = Math.max(0, fin - maxBotonesVisibles + 1); } if (inicio > 0) { ul.appendChild(crearBoton('1', 0)); } if (inicio > 1) { const liPuntos = document.createElement('li'); liPuntos.className = 'page-item disabled'; liPuntos.innerHTML = '<span class="page-link">...</span>'; ul.appendChild(liPuntos); } for (let i = inicio; i <= fin; i++) { ul.appendChild(crearBoton(i + 1, i, true, i === paginaActualProd)); } if (fin < totalPaginasProd - 1) { const liPuntos = document.createElement('li'); liPuntos.className = 'page-item disabled'; liPuntos.innerHTML = '<span class="page-link">...</span>'; ul.appendChild(liPuntos); } if (fin < totalPaginasProd - 1) { ul.appendChild(crearBoton(totalPaginasProd, totalPaginasProd - 1)); } ul.appendChild(crearBoton('<span class="d-none d-sm-inline">Siguiente</span> &raquo;', paginaActualProd + 1, paginaActualProd < totalPaginasProd - 1)); divPaginacion.appendChild(ul);
}

// --- Limpiar Filtros (Adaptado a nuevos filtros) ---
function limpiarFiltrosProductos() {
    document.getElementById('filtroDescripcionProd')?.value = '';
    document.getElementById('filtroProveedorProd')?.value = '';
    const perecederoTodos = document.getElementById('perecederoTodos'); if(perecederoTodos) perecederoTodos.checked = true;
    document.getElementById('filtroPrecioMinProd')?.value = '';
    document.getElementById('filtroPrecioMaxProd')?.value = '';

    // Limpiar Select2 de categorías (requiere jQuery)
    if (typeof $ !== 'undefined' && $.fn.select2) { $('#filtroCategoriasProd').val(null).trigger('change'); }
    else { document.getElementById('filtroCategoriasProd')?.value = ''; }

    // Resetear ordenación
    ordenarPorProd = "descripcion"; // Default a descripción
    direccionOrdenProd = "asc";
    document.getElementById('ordenarProdPorDescripcion')?.classList.add('active');
    document.getElementById('ordenarProdPorPrecio')?.classList.remove('active');
    // Quitar active de otros botones de ordenación si los hubiera
    const btnDir = document.getElementById('direccionOrdenProd'); if(btnDir) btnDir.innerHTML = '↑';
    obtenerProductos(0); // Recargar
}

// --- Mostrar Error ---
function mostrarErrorProd(mensaje) {
    const errorDiv = document.getElementById('errorProd'); const tablaProductos = document.getElementById('tablaProductos'); const paginacionDiv = document.getElementById('paginacionProd'); const msgVacio = document.getElementById('mensajeTablaVaciaProd');
    if (errorDiv) { errorDiv.textContent = mensaje; errorDiv.style.display = 'block'; }
    if (tablaProductos) tablaProductos.style.display = 'none'; if (paginacionDiv) paginacionDiv.innerHTML = ''; if (msgVacio) msgVacio.style.display = 'none';
}

// --- Formatear Moneda (Adaptado para posible BigDecimal object) ---
function formatearMonedaProdAdmin(cantidad) {
    if (cantidad === undefined || cantidad === null) return 'N/A';
    // Intenta convertir si es objeto (como puede venir BigDecimal de Jackson) o string
    const numCantidad = typeof cantidad === 'object' && cantidad !== null ? Number(cantidad) :
        (typeof cantidad === 'string' ? parseFloat(cantidad.replace(',', '.')) : parseFloat(cantidad));
    if (isNaN(numCantidad)) return 'N/A';
    try { return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR', minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(numCantidad); }
    catch (e) { console.warn("Error formateando moneda:", numCantidad, e); return 'N/A'; }
}

// --- (Opcional) Cargar opciones para Selects dinámicamente ---
/*
function cargarOpcionesSelect(urlApi, selectId, opcionDefaultTexto, valorKey, textoKey) {
    const select = document.getElementById(selectId);
    if (!select) return;
    fetch(urlApi) // Llama a los endpoints /api/productos/proveedores o /api/productos/categorias
        .then(res => res.ok ? res.json() : Promise.reject(`Error cargando ${selectId}`))
        .then(data => {
            if(opcionDefaultTexto !== null) select.innerHTML = `<option value="">${opcionDefaultTexto}</option>`; // Opción "Todos/as" si aplica
            else select.innerHTML = ''; // Limpiar si es multiple select sin opción default
            data.forEach(item => {
                select.innerHTML += `<option value="${item[valorKey]}">${item[textoKey]}</option>`;
            });
             // Re-inicializar Select2 si se usa para este select y ya estaba inicializado
             if (typeof $ !== 'undefined' && $.fn.select2 && $(select).data('select2')) {
                  $(select).trigger('change.select2'); // Notifica a Select2 que las opciones cambiaron
             }
        })
        .catch(err => console.error(err));
}
*/