function actualizarTotal() {
    const template = document.getElementById("concepto-template");
    const opciones = Array.from(template.options).map(opt => ({
        id: opt.value,
        tipo: opt.dataset.tipo,
        porcentaje: opt.dataset.porcentaje
    }));

    let total = 0;

    const contenedores = document.querySelectorAll("#conceptos-container > div");
    contenedores.forEach(div => {
        const select = div.querySelector("select");
        const input = div.querySelector("input[type='number']");
        const idConcepto = select.value;
        const cantidad = parseFloat(input.value) || 0;
        const porcentaje = parseFloat(input.value) || 0;

        const concepto = opciones.find(opt => opt.id === idConcepto);
        if (concepto) {
            total += concepto.tipo === "DEDUCCION" ? -cantidad : cantidad;
        }
    });

    document.getElementById("totalLiquido").textContent = total.toFixed(2);
    document.getElementById("totalLiquidoHidden").value = total.toFixed(2);
}
