function actualizarTotal() {
    let total = 0;
    const contenedores = document.querySelectorAll("#conceptos-container > div");

    contenedores.forEach(div => {
        const select = div.querySelector("select");
        const cantidadInput = div.querySelector("input[name='cantidad']");
        const idConcepto = select.value;
        const cantidad = parseFloat(cantidadInput.value) || 0;
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