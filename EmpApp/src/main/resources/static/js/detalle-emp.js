// Variables globales
let empleadoData = null;
const urlParams = new URLSearchParams(window.location.search);
const empleadoId = window.location.pathname.split('/').pop() || urlParams.get('id');

// Elementos DOM principales
const cargando = document.getElementById('cargando');
const error = document.getElementById('error');
const detalleEmpleado = document.getElementById('detalleEmpleado');

// Event listeners
document.addEventListener('DOMContentLoaded', cargarDetalleEmpleado);
document.getElementById('btnEliminar').addEventListener('click', mostrarConfirmacionEliminar);

// Función principal para cargar los detalles del empleado
function cargarDetalleEmpleado() {
    if (!empleadoId) {
        mostrarError('No se ha especificado el ID del empleado');
        return;
    }

    mostrarCargando();

    fetch(`http://localhost:8080/empleados/detalle/${empleadoId}`)
        .then(respuesta => {
            if (!respuesta.ok) {
                throw new Error(`Error ${respuesta.status}: ${respuesta.statusText}`);
            }
            return respuesta.json();
        })
        .then(empleado => {
            empleadoData = empleado;
            mostrarDatosEmpleado(empleado);
            ocultarCargando();
            mostrarDetalleEmpleado();
        })
        .catch(err => {
            mostrarError(`Error al cargar datos del empleado: ${err.message}`);
        });
}

// Función para mostrar los datos del empleado en la interfaz
function mostrarDatosEmpleado(empleado) {
    // Actualizar la cabecera del perfil
    document.getElementById('nombreCompleto').textContent = obtenerNombreCompleto(empleado);
    document.getElementById('puestoEmpleado').textContent = empleado.comentarios || 'Sin información';

    const departamentoTexto = empleado.departamento ?
        `${empleado.departamento.nombre} (${empleado.departamento.localidad})` :
        'Sin departamento asignado';
    document.getElementById('departamentoEmpleado').textContent = departamentoTexto;

    // Actualizar estado (activo/inactivo)
    const estadoBadge = document.getElementById('estadoBadge');
    estadoBadge.textContent = empleado.activo ? 'Activo' : 'Inactivo';
    estadoBadge.className = `status-badge ${empleado.activo ? 'status-active' : 'status-inactive'}`;

    // Mostrar foto si existe
    if (empleado.tieneFoto) {
        const fotoEmpleado = document.getElementById('fotoEmpleado');
        fotoEmpleado.innerHTML = '';
        fotoEmpleado.classList.remove('default-photo');

        const img = document.createElement('img');
        img.src = `/empleados/foto/${empleado.id}`;
        img.alt = 'Foto de perfil';
        img.className = 'profile-photo';
        fotoEmpleado.appendChild(img);
    }

    // Información personal
    document.getElementById('idEmpleado').textContent = empleado.id;
    document.getElementById('nombreEmpleado').textContent = empleado.nombre || 'No especificado';

    const apellidos = [empleado.apellido1, empleado.apellido2]
        .filter(Boolean)
        .join(' ');
    document.getElementById('apellidoEmpleado').textContent = apellidos || 'No especificado';

    document.getElementById('fechaNacimientoEmpleado').textContent = formatearFecha(empleado.fechaNacimiento);
    document.getElementById('emailEmpleado').textContent = empleado.email || 'No especificado';

    // Información laboral
    document.getElementById('departamentoDetalle').innerHTML = formatearDepartamento(empleado.departamento);
    document.getElementById('fechaContratacion').textContent = formatearFecha(empleado.periodo?.fechaInicio);
    document.getElementById('fechaCese').textContent = formatearFecha(empleado.periodo?.fechaFin) || 'No aplicable';
    document.getElementById('estadoEmpleado').innerHTML =
        `<span class="${empleado.activo ? 'text-success' : 'text-danger'} fw-bold">
                    ${empleado.activo ? 'Activo' : 'Inactivo'}
                </span>`;
    document.getElementById('comentariosEmpleado').textContent = empleado.comentarios || 'Sin comentarios';

    // Información de jefe
    const jefeElement = document.getElementById('jefeEmpleado');
    if (empleado.nombreJefe) {
        jefeElement.innerHTML = `
                    <a href="/empleado/detalle/${empleado.idJefe}" class="text-decoration-none">
                        ${empleado.nombreJefe}
                    </a>`;
    } else {
        jefeElement.textContent = 'Sin jefe asignado';
    }

    // Información de subordinados (requeriría otra petición AJAX)
    document.getElementById('subordinadosEmpleado').innerHTML =
        '<p class="fst-italic text-muted">Información no disponible</p>';

    // Especialidades
    const especialidadesContainer = document.getElementById('especialidadesEmpleado');
    if (empleado.especialidades && empleado.especialidades.length > 0) {
        empleado.especialidades.forEach(esp => {
            const badge = document.createElement('span');
            badge.className = 'especialidad-badge';
            badge.textContent = esp.nombre;
            especialidadesContainer.appendChild(badge);
        });
    } else {
        especialidadesContainer.innerHTML =
            '<p class="text-muted fst-italic">No hay especialidades asignadas</p>';
    }

    // Información financiera
    document.getElementById('salarioEmpleado').textContent = formatearMoneda(empleado.salario);
    document.getElementById('comisionEmpleado').textContent = formatearMoneda(empleado.comision);

    const totalSalario = (empleado.salario || 0) + (empleado.comision || 0);
    document.getElementById('totalEmpleado').textContent = formatearMoneda(totalSalario);

    // Información bancaria
    document.getElementById('entidadBancariaEmpleado').textContent =
        empleado.entidadBancaria ?
            `${empleado.entidadBancaria.nombre} (${empleado.entidadBancaria.codigo})` :
            'No especificada';

    document.getElementById('ibanEmpleado').textContent =
        empleado.cuentaCorriente?.iban || 'No especificado';

    document.getElementById('tipoTarjetaEmpleado').textContent =
        empleado.tipoTarjetaCredito?.tipoTarjetaCredito || 'No especificado';

    document.getElementById('numeroTarjetaEmpleado').textContent =
        empleado.tarjetaCredito?.numero || 'No especificado';

    const caducidad = empleado.tarjetaCredito?.mesCaducidad && empleado.tarjetaCredito?.anioCaducidad ?
        `${empleado.tarjetaCredito.mesCaducidad}/${empleado.tarjetaCredito.anioCaducidad}` :
        'No especificado';
    document.getElementById('caducidadTarjetaEmpleado').textContent = caducidad;

    // Actualizar enlace de edición
    // document.getElementById('btnEditar').href = `/empleado/editar/${empleado.id}`;
}

// Función para mostrar el diálogo de confirmación al eliminar
// function mostrarConfirmacionEliminar() {
//     if (confirm('¿Está seguro de que desea eliminar este empleado? Esta acción no se puede deshacer.')) {
//         eliminarEmpleado();
//     }
// }

// Función para eliminar el empleado
// function eliminarEmpleado() {
//     fetch(`/empleado/${empleadoId}`, {
//         method: 'DELETE',
//         headers: {
//             'Content-Type': 'application/json'
//         }
//     })
//         .then(respuesta => {
//             if (!respuesta.ok) {
//                 throw new Error('Error al eliminar el empleado');
//             }
//             window.location.href = '/empleados/listado';
//         })
//         .catch(err => {
//             alert(`Error al eliminar el empleado: ${err.message}`);
//         });
// }

// Funciones auxiliares
function obtenerNombreCompleto(empleado) {
    const partes = [empleado.nombre, empleado.apellido1, empleado.apellido2]
        .filter(Boolean);
    return partes.join(' ') || 'Sin nombre';
}

function formatearFecha(textoFecha) {
    if (!textoFecha) return 'No especificado';
    const fecha = new Date(textoFecha);
    return fecha.toLocaleDateString('es-ES');
}

function formatearMoneda(cantidad) {
    if (cantidad === undefined || cantidad === null) return 'No especificado';
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR'
    }).format(cantidad);
}

function formatearDepartamento(departamento) {
    if (!departamento) return 'No especificado';
    return `
                <a href="/departamento/detalle/${departamento.id}" class="text-decoration-none">
                    ${departamento.nombre}
                </a>
                <div class="text-muted small">
                    Código: ${departamento.codigo},
                    Localidad: ${departamento.localidad}
                </div>
            `;
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