function agregarConcepto() {
    const container = document.getElementById("conceptos-container");
    const div = document.createElement("div");
    div.className = "concepto-item";

    // Clonar las opciones de conceptos disponibles (excluyendo el Salario base)
    const conceptoOptions = document.getElementById("conceptos-restantes").innerHTML;

    div.innerHTML = `
        <div class="row mb-3">
            <div class="col-md-3">
                <label>Concepto:</label>
                <select name="conceptoId" class="form-control" onchange="actualizarCamposPorTipo(this)">
                    <option value="">Seleccione un concepto</option>
                    ${conceptoOptions}
                </select>
            </div>
            <div class="col-md-3">
                <label>Porcentaje (%):</label>
                <input type="text" name="porcentaje" class="form-control" oninput="actualizarCantidadSiNecesario(this)">
            </div>
            <div class="col-md-3">
                <label>Cantidad (€):</label>
                <input type="text" name="cantidad" class="form-control" oninput="actualizarPorcentajeSiNecesario(this)">
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <button type="button" class="btn btn-danger btn-sm" onclick="this.closest('.concepto-item').remove(); actualizarTotal()">Eliminar</button>
            </div>
        </div>
    `;

    container.appendChild(div);

    // Configura los campos basados en el tipo de concepto seleccionado cuando se seleccione uno
    const selectElement = div.querySelector("select");
    selectElement.addEventListener("change", function() {
        actualizarCamposPorTipo(this);
    });
}

// Función para actualizar los campos según el tipo de concepto
function actualizarCamposPorTipo(selectElement) {
    const div = selectElement.closest(".concepto-item");
    const tipoConcepto = selectElement.options[selectElement.selectedIndex].dataset.tipo;
    const porcentajeInput = div.querySelector("input[name='porcentaje']");
    const cantidadInput = div.querySelector("input[name='cantidad']");

    // Restablecer valores y estados
    cantidadInput.value = "";
    porcentajeInput.value = "";
    cantidadInput.disabled = false;
    porcentajeInput.disabled = false;

    if (tipoConcepto === "INGRESO") {
        // Para ingresos: cantidad editable, porcentaje bloqueado
        porcentajeInput.disabled = true;
        porcentajeInput.value = "0";
    } else if (tipoConcepto === "DEDUCCION") {
        // Para deducciones: ambos campos editables
        const porcentajeDefault = selectElement.options[selectElement.selectedIndex].dataset.porcentaje;
        if (porcentajeDefault) {
            porcentajeInput.value = porcentajeDefault;
            // Calcular la cantidad basada en el salario base
            calcularCantidadDesdeProcentaje(porcentajeInput);
        }
    }

    actualizarTotal();
}

// Función para actualizar el porcentaje cuando cambia la cantidad (solo para DEDUCCIONES)
function actualizarPorcentajeSiNecesario(cantidadInput) {
    const div = cantidadInput.closest(".concepto-item");
    const selectElement = div.querySelector("select");
    const tipoConcepto = selectElement.options[selectElement.selectedIndex].dataset.tipo;
    const porcentajeInput = div.querySelector("input[name='porcentaje']");

    if (tipoConcepto === "DEDUCCION") {
        // Obtener el salario base
        const salarioBase = obtenerSalarioBase();
        if (salarioBase > 0 && cantidadInput.value) {
            const cantidad = parseFloat(cantidadInput.value.replace(',', '.'));
            const porcentaje = (cantidad / salarioBase) * 100;
            porcentajeInput.value = porcentaje.toFixed(2);
        }
    }

    actualizarTotal();
}

// Función para actualizar la cantidad cuando cambia el porcentaje (solo para DEDUCCIONES)
function actualizarCantidadSiNecesario(porcentajeInput) {
    calcularCantidadDesdeProcentaje(porcentajeInput);
    actualizarTotal();
}

// Función para calcular la cantidad a partir del porcentaje
function calcularCantidadDesdeProcentaje(porcentajeInput) {
    const div = porcentajeInput.closest(".concepto-item");
    const selectElement = div.querySelector("select");
    const tipoConcepto = selectElement.options[selectElement.selectedIndex].dataset.tipo;
    const cantidadInput = div.querySelector("input[name='cantidad']");

    if (tipoConcepto === "DEDUCCION") {
        // Obtener el salario base
        const salarioBase = obtenerSalarioBase();
        if (salarioBase > 0 && porcentajeInput.value) {
            const porcentaje = parseFloat(porcentajeInput.value.replace(',', '.'));
            const cantidad = (porcentaje / 100) * salarioBase;
            cantidadInput.value = cantidad.toFixed(2);
        }
    }
}

// Función para obtener el salario base actual
function obtenerSalarioBase() {
    const salarioBaseDiv = document.querySelector("#salario-base-container");
    if (salarioBaseDiv) {
        const cantidadInput = salarioBaseDiv.querySelector("input[name='cantidad']");
        return parseFloat(cantidadInput.value.replace(',', '.')) || 0;
    }
    return 0;
}

// Función para inicializar el salario base al cargar la página
function inicializarSalarioBase() {
    const container = document.getElementById("salario-base-container");

    // Obtener el ID y el valor del salario base desde los datos del servidor
    const salarioBaseId = container.dataset.salarioBaseId;
    const salarioBaseValor = parseFloat(container.dataset.salarioBaseValor) || 1184.00;

    container.innerHTML = `
        <div class="row mb-3">
            <div class="col-md-3">
                <label>Concepto:</label>
                <select name="conceptoId" class="form-control" disabled>
                    <option value="${salarioBaseId}" data-tipo="INGRESO" selected>Salario base</option>
                </select>
                <input type="hidden" name="conceptoId" value="${salarioBaseId}">
            </div>
            <div class="col-md-3">
                <label>Porcentaje (%):</label>
                <input type="text" name="porcentaje" class="form-control" value="0" disabled>
            </div>
            
            <div class="col-md-3">
                <label>Cantidad (€):</label>
                <input type="text" name="cantidad" class="form-control" value="${salarioBaseValor.toFixed(2)}" readonly>
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <!-- No hay botón de eliminar para salario base -->
            </div>
        </div>
    `;

    actualizarTotal();
}