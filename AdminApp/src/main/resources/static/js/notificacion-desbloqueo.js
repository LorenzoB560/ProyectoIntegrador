// Path: ProyectoIntegrador/AdminApp/src/main/resources/static/js/notificacion-desbloqueo.js
document.addEventListener('DOMContentLoaded', function() {
    // Intervalo para verificar si hay notificaciones (ej. cada 1 minuto)
    const CHECK_INTERVAL_MS = 60000; // 60,000 milisegundos = 1 minuto

    function verificarYNotificarDesbloqueos() {
        // Asegúrate que la URL base sea la correcta si tu app tiene un context path
        // const baseUrl = window.appBaseUrl || ''; // Si defines appBaseUrl globalmente
        fetch(`/api/empleados/desbloqueados-recientemente`) // Ajusta si tienes context path
            .then(response => {
                if (response.status === 204) { // 204 No Content
                    return null; // No hay empleados desbloqueados recientemente
                }
                if (!response.ok) {
                    throw new Error('Error al verificar empleados desbloqueados: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (data && data.nombres && data.nombres.length > 0) {
                    let mensajeAlerta;
                    if (data.nombres.length === 1) {
                        mensajeAlerta = `El empleado ${data.nombres[0]} ha sido desbloqueado automáticamente.`;
                    } else if (data.nombres.length <= 5) { // Mostrar nombres si son pocos
                        mensajeAlerta = `Los siguientes empleados han sido desbloqueados automáticamente:\n- ${data.nombres.join('\n- ')}`;
                    } else { // Mensaje genérico si son muchos
                        mensajeAlerta = `${data.nombres.length} empleados que estaban bloqueados han sido desbloqueados automáticamente.`;
                    }
                    // Puedes usar una librería de notificaciones más elegante (ej. Toastr, SweetAlert)
                    alert(mensajeAlerta);
                }
            })
            .catch(error => {
                console.error('Error en la función de notificación de desbloqueo:', error);
                // Considera si quieres notificar al admin sobre este error, pero puede ser ruidoso.
            });
    }

    // Verificar al cargar la página y luego periódicamente
    setTimeout(verificarYNotificarDesbloqueos, 2000); // Una pequeña demora inicial
    setInterval(verificarYNotificarDesbloqueos, CHECK_INTERVAL_MS);
});