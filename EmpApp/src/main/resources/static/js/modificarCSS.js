document.addEventListener("DOMContentLoaded", function () {
    const botones = document.querySelectorAll('input[name="modificarCSS"]');
    const hojaEstilo = document.getElementById("estilos");

    // Cargar estado guardado en sessionStorage
    const estadoGuardado = sessionStorage.getItem("cssActivo");

    if (estadoGuardado === "false") {
        hojaEstilo.disabled = true;
        document.querySelector('input[value="off"]').checked = true;
    } else {
        hojaEstilo.disabled = false;
        document.querySelector('input[value="on"]').checked = true;
    }

    // Cambiar y guardar estado al cambiar el botón
    botones.forEach(boton => {
        boton.addEventListener("change", function () {
            const activo = this.value === "on";
            hojaEstilo.disabled = !activo;
            sessionStorage.setItem("cssActivo", activo);
        });
    });
});
