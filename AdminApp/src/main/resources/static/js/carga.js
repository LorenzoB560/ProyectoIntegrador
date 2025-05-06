document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const archivoInput = document.getElementById("archivo");

    form.addEventListener("submit", function (e) {
        e.preventDefault(); // Evita el envío normal del formulario

        const archivo = archivoInput.files[0];
       /* if (!archivo) {
            mostrarMensaje("Por favor selecciona un archivo.", "error");
            return;
        }*/

        const formData = new FormData();
        formData.append("archivo", archivo);

        fetch("/adminapp/carga-masiva", {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => { throw new Error(err); });
                }
                return response.text();
            })
            .then(data => {
                mostrarMensaje("✅ " + data, "exito");
                archivoInput.value = ""; // limpia el input
            })
            .catch(error => {
                mostrarMensaje("❌ Error: " + error.message, "error");
            });
    });

    function mostrarMensaje(mensaje, tipo) {
        const contenedor = document.getElementById("mensaje");
        contenedor.textContent = mensaje;
        contenedor.className = tipo === "exito" ? "mensaje-exito" : "mensaje-error";
    }
});
