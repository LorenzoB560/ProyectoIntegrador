document.addEventListener("DOMContentLoaded", function () {
    const botones = document.querySelectorAll('input[name="modificarCSS"]');
    const hojaEstilo = document.getElementById("estilos");

    // Cargar estado guardado en localStorage
    const estadoGuardado = localStorage.getItem("cssActivo");

    // Determinar cuál hoja de estilo usar al estar guardado en el sessionStorage
    if (estadoGuardado === "true") {
        hojaEstilo.href = "/css/registro-empleado-estilos.css";  // Ruta a CSS por defecto
        document.querySelector('input[value="on"]').checked = true;
    } else {
        hojaEstilo.href = "/css/registro-empleado-sobrio.css";  // Ruta a CSS alternativo
        document.querySelector('input[value="off"]').checked = true;
    }

    // Cambiar y guardar estado al cambiar el botón
    botones.forEach(boton => {
        boton.addEventListener("change", function () {
            const activo = this.value === "on";

            // Cambiar la hoja de estilo según el valor del botón
            if (activo) {
                hojaEstilo.href = "/css/registro-empleado-estilos.css";  // Usar el CSS por defecto
            } else {
                hojaEstilo.href = "/css/registro-empleado-sobrio.css";  // Usar el CSS alternativo
            }

            // Guardar el estado en sessionStorage
            localStorage.setItem("cssActivo", activo);
        });
    });
});
