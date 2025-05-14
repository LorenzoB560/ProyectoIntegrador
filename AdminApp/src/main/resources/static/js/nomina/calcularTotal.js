function actualizarTotal() {
    let total = 0;

    // Primero sumar el salario base
    const salarioBaseDiv = document.querySelector("#salario-base-container");
    if (salarioBaseDiv) {
        const cantidadInputSB = salarioBaseDiv.querySelector("input[name='cantidad']");
        const cantidadSB = parseFloat(cantidadInputSB.value.replace(',', '.')) || 0;
        total += cantidadSB;
    }

    // Luego procesar los demás conceptos
    const contenedores = document.querySelectorAll("#conceptos-container > div.concepto-item");

    contenedores.forEach(div => {
        const select = div.querySelector("select");
        const cantidadInput = div.querySelector("input[name='cantidad']");
        const idConcepto = select.value;
        const cantidad = parseFloat(cantidadInput.value.replace(',', '.')) || 0;
        const tipoConcepto = select.options[select.selectedIndex].dataset.tipo;

        if (tipoConcepto === "DEDUCCION") {
            total -= cantidad;
        } else {
            total += cantidad;
        }
    });

    document.getElementById("totalLiquido").textContent = total.toFixed(2);
    document.getElementById("totalLiquidoHidden").value = total.toFixed(2);
}