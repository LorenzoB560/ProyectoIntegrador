// static/js/listado-prod.js

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
        default:
            // Por ejemplo, si el default es descripción:
            if (!btnActivoId && ordenarPorProd !== "descripcion") {
            } else if (!btnActivoId) {
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
        asignarEventListenersAccionesProd(); // Llamar incluso si está vacío para limpiar listeners anteriores si es necesario
        return;
    }

    productos.forEach(prod => {
        const fila = document.createElement('tr');

        // Preparar datos del ProductoDTO
        const productoId = prod.id || 'N/A';
        const productoDescripcion = prod.descripcion || 'N/A';
        const precioFormateado = formatearMonedaProd(prod.precio);

        const nombreProveedor = (prod.proveedor && prod.proveedor.nombre) ? prod.proveedor.nombre : 'N/A';

        const marcaProducto = prod.marca || 'N/A';

        let categoriasHtml = 'N/A';
        if (prod.categoria && Array.isArray(prod.categoria) && prod.categoria.length > 0) {
            categoriasHtml = prod.categoria.map(cat => `<span class="badge bg-info text-dark me-1">${cat.nombre || ''}</span>`).join(' ');
        } else if (prod.categoria && typeof prod.categoria === 'object' && !Array.isArray(prod.categoria) && Object.keys(prod.categoria).length > 0) {
            categoriasHtml = `<span class="badge bg-info text-dark me-1">${prod.categoria.nombre || ''}</span>`;
        }

        let segundaManoHtml = '<i class="bi bi-question-circle text-muted"></i>';
        if (prod.segundaMano === true) segundaManoHtml = '<i class="bi bi-check-circle-fill text-success"></i> Sí';
        else if (prod.segundaMano === false) segundaManoHtml = '<i class="bi bi-x-circle-fill text-danger"></i> No';

        const unidadesProducto = prod.unidades ?? 'N/A'; // Usar '??' para manejar null o undefined

        const urlBaseAcciones = '/adminapp';
        const nombreParaConfirm = productoDescripcion.replace(/["`]/g, ''); // Quitar comillas dobles y backticks para confirm
        const nombreParaAttr = productoDescripcion.replace(/"/g, '&quot;'); // Escapar comillas dobles para atributo HTML
        const accionesHtml = `
            <div>
                <a href="${urlBaseAcciones}/detalle/${productoId}" class="btn btn-sm btn-primary me-2" title="Ver Detalle">
                    <i class="bi bi-eye"></i>
                </a>
                <a  href="#" class="btn btn-danger me-2 btn-eliminar-prod-js" 
                        data-product-id="${productoId}" 
                        data-product-name="${nombreParaAttr}" 
                        title="Eliminar">
                    <i class="bi bi-trash me-1"></i>
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
    asignarEventListenersAccionesProd();
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
function asignarEventListenersAccionesProd() {
    // --- Listener para botones de Eliminar Producto ---
    document.querySelectorAll('.btn-eliminar-prod-js').forEach(button => {
        // Clonar y reemplazar para evitar listeners duplicados si esta función se llama múltiples veces
        // (Importante si se llama repetidamente en cada recarga de tabla)
        const clone = button.cloneNode(true);
        button.parentNode.replaceChild(clone, button);

        clone.addEventListener('click', (event) => {
            event.preventDefault(); // Prevenir cualquier acción por defecto
            const productId = clone.getAttribute('data-product-id');
            const productName = clone.getAttribute('data-product-name');

            if (!productId) {
                console.error("ID de producto no encontrado en el botón de eliminar.");
                alert("Error: No se pudo obtener la ID del producto a eliminar.");
                return;
            }

            // Ajustar el nombre para el 'confirm' si contiene HTML escapado
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = productName || '(Sin nombre)'; // Decodificar HTML si es necesario
            const decodedProductName = tempDiv.textContent || tempDiv.innerText || '(Sin nombre)';


            // --- 1. Ventana de Confirmación ---
            // Usar el nombre decodificado en el mensaje
            if (confirm(`¿Está seguro de eliminar el producto "${decodedProductName}" (ID: ${productId})? Esta acción no se puede deshacer.`)) {

                // --- 2. Llamada al Endpoint DELETE ---
                const url = `/productos/eliminar/${productId}`;


                // Deshabilitar botón y mostrar carga (opcional)
                clone.disabled = true;
                const originalHtml = clone.innerHTML;
                clone.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';

                fetch(url, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok || response.status === 204) { // 200 OK o 204 No Content son éxito para DELETE
                            return null;
                        } else {
                            // Intentar obtener el mensaje del cuerpo del error
                            return response.text().then(text => {
                                let errorMsg = `Error ${response.status}`;
                                if (text) {
                                    try {
                                        const errorBody = JSON.parse(text);
                                        errorMsg = errorBody?.message || errorBody?.error || text;
                                    } catch (e) {
                                        errorMsg = text; // Si no es JSON, usar el texto plano
                                    }
                                }
                                throw new Error(errorMsg);
                            });
                        }
                    })
                    .then(() => {
                        // --- 3. Acciones en caso de ÉXITO ---
                        console.log(`Producto ${productId} eliminado del servidor.`);
                        alert(`Producto "${decodedProductName}" eliminado correctamente.`);

                        // --- INICIO: Eliminación dinámica del DOM ---
                        const filaParaEliminar = clone.closest('tr'); // 'clone' es el botón de eliminar que se presionó
                        if (filaParaEliminar) {
                            filaParaEliminar.remove();
                            totalElementosProd--; // Decrementar el contador total de productos

                            // Actualizar el texto del contador de resultados
                            const elementosEnPaginaActual = document.getElementById('cuerpoTablaProd').rows.length;
                            document.getElementById('contadorResultadosProd').textContent =
                                `Mostrando ${elementosEnPaginaActual} de ${totalElementosProd} productos - Página ${paginaActualProd + 1} de ${totalPaginasProd || 1}`;

                            // Si la tabla queda vacía en la página actual
                            if (elementosEnPaginaActual === 0) {
                                if (totalElementosProd > 0 && paginaActualProd > 0) {
                                    obtenerProductos(paginaActualProd - 1);
                                } else if (totalElementosProd === 0) {
                                    // No quedan productos en ninguna página
                                    document.getElementById('tablaProductos').style.display = 'none';
                                    document.getElementById('mensajeTablaVaciaProd').style.display = 'block';
                                    document.getElementById('paginacionProd').innerHTML = ''; // Limpiar paginación
                                    totalPaginasProd = 0;
                                    document.getElementById('contadorResultadosProd').textContent = "No hay productos para mostrar.";
                                } else {
                                    obtenerProductos(0);
                                }
                            } else {
                                const nuevoTotalPaginas = Math.ceil(totalElementosProd / tamañoPaginaProd);
                                if (nuevoTotalPaginas !== totalPaginasProd) {
                                    totalPaginasProd = nuevoTotalPaginas;
                                    crearControlesPaginacionProductos();
                                }
                            }
                        } else {
                            // Si no se encontró la fila, por si acaso, recargar.
                            obtenerProductos(paginaActualProd);
                        }
                        // --- FIN: Eliminación dinámica del DOM ---
                    })
                    .catch(error => {
                        // --- 4. Acciones en caso de ERROR ---
                        console.error(`Error al eliminar producto ${productId}:`, error);
                        alert(`Error al eliminar producto: ${error.message}`);
                    })
                    .finally(() => {
                        if (!clone.disabled) return; // Ya rehabilitado o no existe
                        try {
                            clone.disabled = false;
                            clone.innerHTML = originalHtml;
                        } catch (e) {
                            // El botón puede haber sido eliminado por la recarga de la tabla
                            console.warn("No se pudo rehabilitar el botón de eliminar, probablemente ya fue reemplazado.");
                        }
                    });
            } else {
                console.log(`Eliminación cancelada para producto ${productId}.`);
            }
        });
    });
}

