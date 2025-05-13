function agregarConcepto() {
    const container = document.getElementById("conceptos-container");
    const div = document.createElement("div");

    div.innerHTML = `
        <label>Concepto:</label>
        <select name="conceptoId" onchange="actualizarTotal()">
            ${document.getElementById("concepto-template").innerHTML}
        </select>

        <label>Cantidad:</label>
        <input type="text" name="cantidad" oninput="actualizarTotal()">
        <button type="button" onclick="this.parentElement.remove(); actualizarTotal()">Eliminar</button>
        <br><br>
    `;

    container.appendChild(div);
    actualizarTotal();
}
