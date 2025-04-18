function confirmarVolver(event) {
    // Mostrar la ventana de confirmación
    const confirmacion = confirm("¿Estás seguro de que quieres volver al principio?");
    if (!confirmacion) {
        // Si el usuario elige "Cancelar", se evita la acción por defecto
        event.preventDefault();
    }
    // Si elige "Aceptar", no se hace nada y el enlace se sigue
}

// Esperar a que el DOM cargue para asignar el evento
window.addEventListener("DOMContentLoaded", () => {
    const enlace = document.getElementById("volverAlPrincipio");
    enlace.addEventListener("click", confirmarVolver);
});

function confimarGuardar(event) {
    // Mostrar la ventana de confirmación
    const confirmacion = confirm("¿Estás seguro de que quieres guardar al empleado en la base de datos?");
    if (!confirmacion) {
        // Si el usuario elige "Cancelar", se evita la acción por defecto
        event.preventDefault();
    }
    // Si elige "Aceptar", no se hace nada y el enlace se sigue
}

// Esperar a que el DOM cargue para asignar el evento
window.addEventListener("DOMContentLoaded", () => {
    const enlace = document.getElementById("guardarEmpleado");
    enlace.addEventListener("click", confimarGuardar);
});