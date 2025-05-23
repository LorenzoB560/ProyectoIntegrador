
// Variables globales
let empleadoData = null;
var urlParams = new URLSearchParams(window.location.search);
var empleadoId = window.location.pathname.split('/').pop() || urlParams.get('id');

// Elementos DOM principales
const cargando = document.getElementById('cargando');
const error = document.getElementById('error');
const detalleEmpleado = document.getElementById('detalleEmpleado');

// Event listeners
document.addEventListener('DOMContentLoaded', cargarDetalleEmpleado);

// Función principal para cargar los detalles del empleado
function cargarDetalleEmpleado() {
    if (!empleadoId) {
        mostrarError('No se ha especificado el ID del empleado');
        return;
    }

    mostrarCargando();

    fetch(`/adminapp/empleados/detalle/${empleadoId}`)
        .then(respuesta => {
            if (!respuesta.ok) {
                throw new Error(`Error ${respuesta.status}: ${respuesta.statusText}`);
            }
            return respuesta.json();
        })
        .then(empleado => {
            empleadoData = empleado;
            ocultarCargando();
            mostrarDetalleEmpleado();
        })
        .catch(err => {
            mostrarError(`Error al cargar datos del empleado: ${err.message}`);
        });
}

// Funciones de control de UI
function mostrarCargando() {
    cargando.style.display = 'flex';
    detalleEmpleado.style.display = 'none';
    error.style.display = 'none';
}

function ocultarCargando() {
    cargando.style.display = 'none';
}

function mostrarDetalleEmpleado() {
    detalleEmpleado.style.display = 'block';
}

function mostrarError(mensaje) {
    error.textContent = mensaje;
    error.style.display = 'block';
    cargando.style.display = 'none';
    detalleEmpleado.style.display = 'none';
}

// Añadir esta nueva función al archivo
function cargarSubordinados(idJefe) {
    const subordinadosContainer = document.getElementById('subordinadosEmpleado');
    subordinadosContainer.innerHTML = '<p class="text-muted">Cargando subordinados...</p>';

    fetch(`/adminapp/empleados/${idJefe}/subordinados`)
        .then(respuesta => {
            if (!respuesta.ok) {
                throw new Error(`Error ${respuesta.status}: ${respuesta.statusText}`);
            }
            return respuesta.json();
        })
        .then(subordinados => {
            mostrarSubordinados(subordinados, subordinadosContainer);
        })
        .catch(err => {
            subordinadosContainer.innerHTML =
                `<p class="text-danger">Error al cargar subordinados: ${err.message}</p>`;
        });
}