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

    // --- NUEVOS LISTENERS PARA ORDENACIÓN ---
    const btnOrdenarNombre = document.getElementById('ordenarProdPorNombre');
    const btnOrdenarPrecio = document.getElementById('ordenarProdPorPrecio');
    const btnDireccion = document.getElementById('direccionOrdenProd');

    if (btnOrdenarNombre) {
        btnOrdenarNombre.addEventListener('click', () => {
            if (ordenarPorProd !== "nombre") {
                ordenarPorProd = "nombre";
                direccionOrdenProd = "asc"; // Resetear dirección al cambiar campo
                if(btnDireccion) btnDireccion.innerHTML = '↑';
                btnOrdenarNombre.classList.add('active');
                btnOrdenarPrecio?.classList.remove('active');
                obtenerProductos(0); // Recargar con nueva ordenación
            }
        });
    }

    if (btnOrdenarPrecio) {
        btnOrdenarPrecio.addEventListener('click', () => {
            if (ordenarPorProd !== "precio") {
                ordenarPorProd = "precio";
                direccionOrdenProd = "asc"; // Resetear dirección
                if(btnDireccion) btnDireccion.innerHTML = '↑';
                btnOrdenarPrecio.classList.add('active');
                btnOrdenarNombre?.classList.remove('active');
                obtenerProductos(0); // Recargar
            }
        });
    }

    if (btnDireccion) {
        btnDireccion.addEventListener('click', () => {
            // Cambiar dirección
            direccionOrdenProd = (direccionOrdenProd === "asc") ? "desc" : "asc";
            // Actualizar flecha
            btnDireccion.innerHTML = (direccionOrdenProd === "asc") ? '↑' : '↓';
            // Recargar datos
            obtenerProductos(0);
        });
    }
    // --- FIN NUEVOS LISTENERS ---

});

// --- Función Principal para Obtener Productos ---
function obtenerProductos(pagina) {
    if (pagina < 0) return;
    paginaActualProd = pagina;

    // Leer filtros
    const nombre = document.getElementById('filtroNombreProd')?.value.trim() || '';
    const tipo = document.getElementById('filtroTipoProd')?.value || '';
    const precio = document.getElementById('filtroPrecioProd')?.value || '';

    // Construir URL Relativa
    let url = new URL('/productos/listado', window.location.origin);

    // Añadir parámetros de filtro
    if (nombre) url.searchParams.append('nombre', nombre);
    if (tipo) url.searchParams.append('tipo', tipo);
    if (precio) url.searchParams.append('precio', precio);

    // Añadir parámetros de paginación y ordenación (¡YA INCLUIDOS!)
    url.searchParams.append('page', paginaActualProd);
    url.searchParams.append('size', tamanioPaginaProd);
    url.searchParams.append('sortBy', ordenarPorProd); // Usa la variable global
    url.searchParams.append('sortDir', direccionOrdenProd); // Usa la variable global

    // --- Elementos UI y Fetch (sin cambios respecto a la versión anterior) ---
    const cargandoDiv = document.getElementById('cargandoProd');
    const tablaProductos = document.getElementById('tablaProductos');
    const errorDiv = document.getElementById('errorProd');
    const contadorSpan = document.getElementById('contadorResultadosProd');
    const paginacionDiv = document.getElementById('paginacionProd');
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');

    if(cargandoDiv) cargandoDiv.style.display = 'block';
    if(tablaProductos) tablaProductos.style.display = 'none';
    if(errorDiv) errorDiv.style.display = 'none';
    if(contadorSpan) contadorSpan.textContent = '';
    if(paginacionDiv) paginacionDiv.innerHTML = '';
    if(cuerpoTabla) cuerpoTabla.innerHTML = '';

    fetch(url)
        .then(respuesta => {
            if (!respuesta.ok) {
                return respuesta.text().then(text => { throw new Error(text || `Error ${respuesta.status}`); });
            }
            return respuesta.json();
        })
        .then(datos => {
            paginaActualProd = datos.number;
            totalPaginasProd = datos.totalPages;
            totalElementosProd = datos.totalElements;
            llenarTablaProductos(datos.content);
            crearControlesPaginacionProductos();
            asignarEventListenersAccionesProd();
            if(contadorSpan) {
                const inicio = datos.numberOfElements > 0 ? (paginaActualProd * tamanioPaginaProd) + 1 : 0;
                const fin = inicio + datos.numberOfElements - 1;
                const fin_mostrar = (fin < inicio) ? inicio : fin; // Corrección para 0 elementos
                contadorSpan.textContent = `Mostrando ${inicio}-${fin_mostrar} de ${totalElementosProd} productos - Página ${paginaActualProd + 1} de ${totalPaginasProd || 1}`;
            }
            if (tablaProductos) tablaProductos.style.display = 'table';
        })
        .catch(error => {
            console.error("Error al obtener productos:", error);
            mostrarErrorProd(`Error al obtener datos: ${error.message}`);
        })
        .finally(() => {
            if(cargandoDiv) cargandoDiv.style.display = 'none';
        });
}

// --- Función llenarTablaProductos (sin cambios) ---
function llenarTablaProductos(productos) {
    const cuerpoTabla = document.getElementById('cuerpoTablaProd');
    const tabla = document.getElementById('tablaProductos');
    if (!cuerpoTabla || !tabla) return;
    cuerpoTabla.innerHTML = '';
    if (!productos || productos.length === 0) {
        const numColumnas = tabla.querySelector('thead tr')?.cells.length || 6;
        cuerpoTabla.innerHTML = `<tr><td colspan="${numColumnas}" class="text-center p-3 fst-italic text-muted">No se encontraron productos con los criterios seleccionados.</td></tr>`;
    } else {
        productos.forEach(prod => {
            const fila = document.createElement('tr');
            const descCorta = prod.descripcion ? (prod.descripcion.length > 60 ? prod.descripcion.substring(0, 60) + '...' : prod.descripcion) : 'N/A';
            const precioFormateado = formatearMonedaProd(prod.precio);
            const detalleUrl = `/producto/detalle/${prod.id}`;
            fila.innerHTML = `
                <td><small class="text-muted">${prod.id || 'N/A'}</small></td>
                <td>${prod.nombre || 'N/A'}</td>
                <td><span class="badge bg-info text-dark">${prod.tipoProductoNombre || 'N/A'}</span></td>
                <td>${precioFormateado}</td>
                <td>${descCorta}</td>
                <td>
                    <a href="${detalleUrl}" class="btn btn-sm btn-primary " title="Ver Detalle">
                        <i class="bi bi-eye"></i> Ver
                    </a>
                    <a id="btnEliminar" href="#" class="btn btn-danger me-2 btn-eliminar-prod-js" 
                        data-product-id="${prod.id}" data-product-name="${prod.nombre}"> 
                        <i class="bi bi-trash me-1"></i> Eliminar
                    </a>
                </td>
            `;
            cuerpoTabla.appendChild(fila);
        });
    }
}
function asignarEventListenersAccionesProd() {
    // --- Listener para botones de Eliminar Producto ---
    document.querySelectorAll('.btn-eliminar-prod-js').forEach(button => {
        // Clonar y reemplazar para evitar listeners duplicados si esta función se llama múltiples veces
        const clone = button.cloneNode(true);
        button.parentNode.replaceChild(clone, button);

        clone.addEventListener('click', (event) => {
            event.preventDefault(); // Prevenir cualquier acción por defecto
            const productId = clone.getAttribute('data-product-id');// Leer data-product-id
            const productName = clone.getAttribute('data-product-name'); // Leer data-product-name

            if (!productId) {
                console.error("ID de producto no encontrado en el botón de eliminar.");
                alert("Error: No se pudo obtener la ID del producto a eliminar.");
                return;
            }

            // --- 1. Ventana de Confirmación ---
            if (confirm(`¿Está seguro de eliminar el producto "${productName}" (ID: ${productId})? Esta acción no se puede deshacer.`)) {

                // --- 2. Llamada al Endpoint DELETE ---
                const url = `/productos/eliminar/${productId}`; // URL del endpoint DELETE en ProductoRestController de AdminApp
                const headers = {
                    // Añadir cabecera CSRF aquí si la estás usando en AdminApp con Spring Security
                    // 'X-CSRF-TOKEN': '...'
                };

                // Deshabilitar botón y mostrar carga (opcional)
                clone.disabled = true;
                const originalHtml = clone.innerHTML; // Guardar icono original
                clone.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';

                fetch(url, {
                    method: 'DELETE',
                    headers: headers
                })
                    .then(response => {
                        // DELETE exitoso puede devolver 200 OK o 204 No Content
                        if (response.ok || response.status === 204) {
                            return null; // Indicar éxito
                        } else {
                            // Si hay error, intentar obtener el mensaje del cuerpo
                            return response.text().then(text => {
                                throw new Error(text || `Error ${response.status}`);
                            });
                        }
                    })
                    .then(() => {
                        // --- 3. Acciones en caso de ÉXITO ---
                        console.log(`Producto ${productId} eliminado.`);
                        alert(`Producto "${productName}" eliminado correctamente.`);
                        // Recargar la tabla para reflejar el cambio
                        obtenerProductos(paginaActualProd); // Llama a la función que recarga la lista
                    })
                    .catch(error => {
                        // --- 4. Acciones en caso de ERROR ---
                        console.error(`Error al eliminar producto ${productId}:`, error);
                        alert(`Error al eliminar producto: ${error.message}`);
                        // Rehabilitar el botón
                        clone.disabled = false;
                        clone.innerHTML = originalHtml; // Restaurar icono/texto original
                    });
            } else {
                // El usuario canceló la confirmación
                console.log(`Eliminación cancelada para producto ${productId}.`);
            }
        });
    });
}
// --- Función crearControlesPaginacionProductos (sin cambios) ---
function crearControlesPaginacionProductos() {
    const divPaginacion = document.getElementById('paginacion');
    divPaginacion.innerHTML = '';

    if (totalPaginasProd <= 1) return; // No mostrar paginación si hay una página o menos

    // Botón anterior
    const botonAnterior = document.createElement('button');
    botonAnterior.innerHTML = '&laquo; Anterior';
    botonAnterior.className = 'btn btn-outline-primary me-2';
    botonAnterior.disabled = paginaActualProd === 0;
    botonAnterior.addEventListener('click', () => obtenerProductos(paginaActualProd - 1));
    divPaginacion.appendChild(botonAnterior);

    // Botones de páginas
    const paginaInicio = Math.max(0, paginaActualProd - 2);
    const paginaFin = Math.min(totalPaginasProd - 1, paginaActualProd + 2);

    for (let i = paginaInicio; i <= paginaFin; i++) {
        const botonPagina = document.createElement('button');
        botonPagina.textContent = i + 1;
        botonPagina.className = i === paginaActualProd ? 'btn btn-primary me-2' : 'btn btn-outline-primary me-2';
        botonPagina.addEventListener('click', () => obtenerProductos(i));
        divPaginacion.appendChild(botonPagina);
    }

    // Botón siguiente
    const botonSiguiente = document.createElement('button');
    botonSiguiente.innerHTML = 'Siguiente &raquo;';
    botonSiguiente.className = 'btn btn-outline-primary';
    botonSiguiente.disabled = paginaActualProd >= totalPaginasProd - 1;
    botonSiguiente.addEventListener('click', () => obtenerProductos(paginaActualProd + 1));
    divPaginacion.appendChild(botonSiguiente);
}

// --- Función limpiarFiltrosProductos (sin cambios) ---
function limpiarFiltrosProductos() {
    const nombreInput = document.getElementById('filtroNombreProd');
    const tipoSelect = document.getElementById('filtroTipoProd');
    const precioInput = document.getElementById('filtroPrecioProd');
    if(nombreInput) nombreInput.value = '';
    if(tipoSelect) tipoSelect.value = '';
    if(precioInput) precioInput.value = '';
    ordenarPorProd = "nombre"; // Resetear orden al limpiar
    direccionOrdenProd = "asc";
    // Actualizar botones visualmente (opcional, o se actualizarán al recargar)
    document.getElementById('ordenarProdPorNombre')?.classList.add('active');
    document.getElementById('ordenarProdPorPrecio')?.classList.remove('active');
    document.getElementById('direccionOrdenProd')?.classList.remove('active');
    const btnDir = document.getElementById('direccionOrdenProd');
    if(btnDir) btnDir.innerHTML = '↑';
    obtenerProductos(0);
}

// --- Función mostrarErrorProd (sin cambios) ---
function mostrarErrorProd(mensaje) {
    const errorDiv = document.getElementById('errorProd');
    const tablaProductos = document.getElementById('tablaProductos');
    const paginacionDiv = document.getElementById('paginacionProd');
    if (errorDiv) { errorDiv.textContent = mensaje; errorDiv.style.display = 'block'; }
    if (tablaProductos) tablaProductos.style.display = 'none';
    if (paginacionDiv) paginacionDiv.innerHTML = '';
}

// --- Función formatearMonedaProd (sin cambios) ---
function formatearMonedaProd(cantidad) {
    if (cantidad === undefined || cantidad === null || isNaN(cantidad)) return 'N/A';
    try { return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR', minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(cantidad); }
    catch (e) { console.warn("Error formateando moneda:", cantidad, e); return 'N/A'; }
}