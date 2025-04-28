// Función para obtener el ID del departamento de la URL
function obtenerIdDepartamentoDeUrl() {
    const ruta = window.location.pathname;
    const segmentos = ruta.split('/');
    return segmentos[segmentos.length - 1];
}

// Función principal para cargar los datos
function cargarDetalleDepartamento() {
    const departamentoId = obtenerIdDepartamentoDeUrl();

    if (!departamentoId) {
        mostrarError("No se encontró el ID del departamento en la URL");
        return;
    }

    mostrarCargando();

    fetch(`http://localhost:9090/departamentos/detalle/${departamentoId}`)
        .then(respuesta => {
            if (!respuesta.ok) {
                throw new Error(`Error ${respuesta.status}: ${respuesta.statusText}`);
            }
            return respuesta.json();
        })
        .then(datos => {
            mostrarDetalleDepartamento(datos);
        })
        .catch(error => {
            mostrarError("Error al cargar los datos: " + error.message);
        });
}

// Función para mostrar los datos del departamento
function mostrarDetalleDepartamento(departamento) {
    document.getElementById('id').textContent = departamento.id;
    document.getElementById('codigo').textContent = departamento.codigo;
    document.getElementById('nombre').textContent = departamento.nombre;
    document.getElementById('localizacion').textContent = departamento.localizacion;
    document.getElementById('direccion').innerHTML = formatearDireccion(departamento.direccion);

    ocultarCargando();
    document.getElementById('detalleDepartamento').style.display = 'block';
}

// Función para formatear la dirección
function formatearDireccion(direccion) {
    if (!direccion) return 'No hay información de dirección';

    return `
                <div class="direccion-detalle">
                    ${direccion.calle ? `${direccion.calle} ${direccion.numero}` : ''}<br>
                    ${direccion.escalera ? `Escalera ${direccion.escalera}` : ''}
                    ${direccion.piso ? `Piso ${direccion.piso}` : ''}
                    ${direccion.letra ? `Letra ${direccion.letra}` : ''}<br>
                    ${direccion.cp ? `CP: ${direccion.cp}` : ''}
                    ${direccion.localidad ? `${direccion.localidad}, ` : ''}
                    ${direccion.pais || ''}
                </div>
            `;
}

// Funciones de control de UI
function mostrarCargando() {
    document.getElementById('cargando').style.display = 'block';
    document.getElementById('detalleDepartamento').style.display = 'none';
    document.getElementById('error').style.display = 'none';
}

function ocultarCargando() {
    document.getElementById('cargando').style.display = 'none';
}

function mostrarError(mensaje) {
    document.getElementById('error').textContent = mensaje;
    document.getElementById('error').style.display = 'block';
    document.getElementById('cargando').style.display = 'none';
}

// Iniciar la carga al cargar la página
document.addEventListener('DOMContentLoaded', cargarDetalleDepartamento);