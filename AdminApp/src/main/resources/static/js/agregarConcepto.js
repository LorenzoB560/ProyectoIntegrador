function agregarConcepto() {
    const container = document.getElementById("conceptos-container");
    const div = document.createElement("div");

    div.innerHTML = `
        <label>Concepto:</label>
        <select name="conceptoId" onchange="actualizarTotal()">
            ${document.getElementById("concepto-template").innerHTML}
        </select>

        <label>Cantidad:</label>
        <input type="number" step="0.01" name="cantidad" value="0" oninput="actualizarTotal()">

        <button type="button" onclick="this.parentElement.remove(); actualizarTotal()">Eliminar</button>
        <br><br>
    `;

    container.appendChild(div);
    actualizarTotal();
}
