// Función para agregar un nuevo concepto a la lista de conceptos
function agregarConcepto() {
    const container = document.getElementById("conceptos-container");
    const div = document.createElement("div");
    div.className = "concepto-item";

    // Se obtienen las opciones de conceptos disponibles desde el DOM (excluyendo el salario base)
    const conceptoOptions = document.getElementById("conceptos-restantes").innerHTML;

    // Se crea la estructura del concepto en HTML, incluyendo el selector, porcentaje y cantidad
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

    // Se agrega el nuevo concepto al contenedor principal
    container.appendChild(div);

    // Se agrega un listener para actualizar los campos cuando se seleccione un concepto
    const selectElement = div.querySelector("select");
    selectElement.addEventListener("change", function() {
        actualizarCamposPorTipo(this);
    });
}

// Función que ajusta los campos según el tipo de concepto seleccionado
function actualizarCamposPorTipo(selectElement) {
    const div = selectElement.closest(".concepto-item");
    const tipoConcepto = selectElement.options[selectElement.selectedIndex].dataset.tipo;
    const porcentajeInput = div.querySelector("input[name='porcentaje']");
    const cantidadInput = div.querySelector("input[name='cantidad']");

    // Se restablecen los valores y se habilitan los campos de porcentaje y cantidad
    cantidadInput.value = "";
    porcentajeInput.value = "";
    cantidadInput.disabled = false;
    porcentajeInput.disabled = false;

    if (tipoConcepto === "INGRESO") {
        // Para ingresos, la cantidad es editable pero el porcentaje está bloqueado
        porcentajeInput.disabled = true;
        porcentajeInput.value = "0";
    } else if (tipoConcepto === "DEDUCCION") {
        // Para deducciones, se permite modificar ambos campos
        const porcentajeDefault = selectElement.options[selectElement.selectedIndex].dataset.porcentaje;
        if (porcentajeDefault) {
            porcentajeInput.value = porcentajeDefault;
            // Se calcula la cantidad correspondiente al porcentaje indicado
            calcularCantidadDesdeProcentaje(porcentajeInput);
        }
    }

    actualizarTotal();
}

// Función que recalcula el porcentaje en función de la cantidad introducida (para deducciones)
function actualizarPorcentajeSiNecesario(cantidadInput) {
    const div = cantidadInput.closest(".concepto-item");
    const selectElement = div.querySelector("select");
    const tipoConcepto = selectElement.options[selectElement.selectedIndex].dataset.tipo;
    const porcentajeInput = div.querySelector("input[name='porcentaje']");

    if (tipoConcepto === "DEDUCCION") {
        const salarioBase = obtenerSalarioBase();
        if (salarioBase > 0 && cantidadInput.value) {
            // Convierte la cantidad introducida a número flotante
            const cantidad = parseFloat(cantidadInput.value.replace(',', '.'));

            // Calcula el porcentaje en base al salario base
            const porcentaje = (cantidad / salarioBase) * 100;

            // Redondea el porcentaje a 2 decimales
            porcentajeInput.value = porcentaje.toFixed(2);
        }
    }

    actualizarTotal(); // Actualiza el cálculo global
}

// Función que recalcula la cantidad en función del porcentaje introducido (para deducciones)
function actualizarCantidadSiNecesario(porcentajeInput) {
    calcularCantidadDesdeProcentaje(porcentajeInput);
    actualizarTotal();
}

// Función que calcula la cantidad basándose en el porcentaje introducido
function calcularCantidadDesdeProcentaje(porcentajeInput) {
    const div = porcentajeInput.closest(".concepto-item");
    const selectElement = div.querySelector("select");
    const tipoConcepto = selectElement.options[selectElement.selectedIndex].dataset.tipo;
    const cantidadInput = div.querySelector("input[name='cantidad']");

    if (tipoConcepto === "DEDUCCION") {
        const salarioBase = obtenerSalarioBase();
        if (salarioBase > 0 && porcentajeInput.value) {
            // Convierte el porcentaje introducido a número flotante
            const porcentaje = parseFloat(porcentajeInput.value.replace(',', '.'));

            // Calcula la cantidad en euros según el porcentaje indicado
            const cantidad = (porcentaje / 100) * salarioBase;

            // Redondea la cantidad a 2 decimales
            cantidadInput.value = cantidad.toFixed(2);
        }
    }
}

// Función que obtiene el salario base desde el DOM
function obtenerSalarioBase() {
    const salarioBaseDiv = document.querySelector("#salario-base-container");
    if (salarioBaseDiv) {
        const cantidadInput = salarioBaseDiv.querySelector("input[name='cantidad']");
        return parseFloat(cantidadInput.value.replace(',', '.')) || 0;
    }
    return 0;
}

// Función que inicializa el salario base al cargar la página
function inicializarSalarioBase() {
    const container = document.getElementById("salario-base-container");

    // Se obtienen los datos del salario base desde el servidor
    const salarioBaseId = container.dataset.salarioBaseId;
    const salarioBaseValor = parseFloat(container.dataset.salarioBaseValor) || 1184.00;

    // Se establece la estructura HTML del salario base (no puede eliminarse ni modificarse el porcentaje)
    container.innerHTML = `
        <div class="row mb-3">
            <div class="col-md-3">
                <label>Concepto:</label>
                <select name="conceptoId" class="form-control" disabled >
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
                <input type="text" name="cantidad" class="form-control" value="${salarioBaseValor.toFixed(2)}" oninput="actualizarTotal()">
            </div>
        </div>
    `;

    actualizarTotal();
}