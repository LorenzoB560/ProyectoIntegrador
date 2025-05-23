// static/js/notificacion-desbloqueo.js
document.addEventListener('DOMContentLoaded', function() {
    const CHECK_INTERVAL_MS = 30000; // Verificar cada 1 minuto

    function verificarDesbloqueos() {
        fetch('/adminapp/empleados/desbloqueados-recientemente') // Ajusta la URL si tienes un context-path
            .then(response => {
                if (response.status === 204) { // No Content
                    return null;
                }
                if (!response.ok) {
                    throw new Error('Error al verificar desbloqueos: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (data && data.nombres && data.nombres.length > 0) {
                    let mensaje = "Los siguientes empleados han sido desbloqueados automáticamente:\n" + data.nombres.join("\n");
                    if (data.nombres.length > 3) {
                        mensaje = `Se han desbloqueado ${data.nombres.length} empleados que estaban bloqueados.`;
                    } else if (data.nombres.length > 0) {
                        mensaje = `Los empleados: ${data.nombres.join(', ')} han sido desbloqueados.`;
                    }
                    alert(mensaje); // Reemplaza alert() con una notificación mejor
                }
            })
            .catch(error => {
                console.error('Error en notificación de desbloqueo:', error);
            });
    }

    // Verificar al cargar y luego periódicamente (puedes ajustar el primer timeout)
    setTimeout(verificarDesbloqueos, 1000); // Pequeña espera inicial
    setInterval(verificarDesbloqueos, CHECK_INTERVAL_MS);
});