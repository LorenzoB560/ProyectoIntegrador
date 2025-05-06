// Espera a que el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {

    const cargandoDiv = document.getElementById('cargandoDetalleProd');
    const errorDiv = document.getElementById('errorDetalleProd');
    const detalleContenedor = document.getElementById('detalleProductoContenedor');

    if (typeof productoId === 'undefined' || productoId === null) {
        mostrarErrorProd('Error: No se pudo obtener el ID del producto.');
        return;
    }

    if (cargandoDiv) cargandoDiv.style.display = 'block';
    if (errorDiv) errorDiv.style.display = 'none';
    if (detalleContenedor) detalleContenedor.style.display = 'none';

    // URL del endpoint REST de EmpApp
    const url = `http://localhost:9090/productos/detalle/${productoId}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || `Error ${response.status}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (cargandoDiv) cargandoDiv.style.display = 'none';
            if (detalleContenedor) detalleContenedor.style.display = 'block';

            rellenarCampo('prod-id', data.id);
            rellenarCampo('prod-nombre', data.nombre);
            rellenarCampo('prod-descripcion', data.descripcion);
            rellenarCampo('prod-precio', formatearMonedaProd(data.precio));
            rellenarCampo('prod-tipo', data.tipoProductoNombre);
            rellenarCampo('prod-tipo-header', data.tipoProductoNombre);

            mostrarCamposEspecificos(data);
        })
        .catch(error => {
            console.error('Error al cargar detalle del producto:', error);
            mostrarErrorProd(`Error al cargar datos: ${error.message}`);
        });
});

function rellenarCampo(idElemento, valor) {
    const elemento = document.getElementById(idElemento);
    if (elemento) {
        elemento.textContent = valor !== null && valor !== undefined ? valor : 'N/D';
    }
}

function mostrarCamposEspecificos(data) {
    document.getElementById('detalles-libro')?.classList.replace('campo-visible', 'campo-especifico');
    document.getElementById('detalles-electronico')?.classList.replace('campo-visible', 'campo-especifico');
    document.getElementById('detalles-ropa')?.classList.replace('campo-visible', 'campo-especifico');

    if (data.tipoProductoNombre === 'Libro') {
        rellenarCampo('prod-autor', data.autor);
        rellenarCampo('prod-editorial', data.editorial);
        document.getElementById('detalles-libro')?.classList.replace('campo-especifico', 'campo-visible');
    } else if (data.tipoProductoNombre === 'Electrónico') {
        rellenarCampo('prod-marca', data.marca);
        document.getElementById('detalles-electronico')?.classList.replace('campo-especifico', 'campo-visible');
    } else if (data.tipoProductoNombre === 'Ropa') {
        rellenarCampo('prod-material', data.material);
        document.getElementById('detalles-ropa')?.classList.replace('campo-especifico', 'campo-visible');
    }
}

function mostrarErrorProd(mensaje) {
    const errorDiv = document.getElementById('errorDetalleProd');
    const cargandoDiv = document.getElementById('cargandoDetalleProd');
    if (errorDiv) {
        errorDiv.textContent = mensaje;
        errorDiv.style.display = 'block';
    }
    if (cargandoDiv) cargandoDiv.style.display = 'none';
    const detalleContenedor = document.getElementById('detalleProductoContenedor');
    if (detalleContenedor) detalleContenedor.style.display = 'none';
}

function formatearMonedaProd(cantidad) {
    if (cantidad === undefined || cantidad === null || isNaN(cantidad)) return 'N/A';
    try {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR'
        }).format(cantidad);
    } catch (e) {
        return 'N/A';
    }
}