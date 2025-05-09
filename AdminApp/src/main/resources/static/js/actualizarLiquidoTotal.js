document.addEventListener("DOMContentLoaded", function () {
    const totalLiquidoInput = document.querySelector("input[name='totalLiquido']");
    const inputsCantidad = document.querySelectorAll("input[id^='concepto_']");

    function actualizarTotalLiquido() {
        let total = 0;

        inputsCantidad.forEach(input => {
            let cantidad = parseFloat(input.value) || 0;
            let conceptoElemento = input.closest("li");
            let tipo = conceptoElemento.getAttribute("data-tipo");

            if (tipo === "DEDUCCION") {
                total -= cantidad;
            } else {
                total += cantidad;
            }
        });

        totalLiquidoInput.value = total.toFixed(2);
    }

    // Escuchar cambios en los inputs para actualizar el total automáticamente
    inputsCantidad.forEach(input => {
        input.addEventListener("input", actualizarTotalLiquido);
    });
});
