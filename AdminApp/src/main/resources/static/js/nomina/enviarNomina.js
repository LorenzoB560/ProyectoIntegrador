document.getElementById("formulario").addEventListener("submit", function (e) {
    e.preventDefault();

    //Obtengo todos los valores que se han introducido
    const idEmpleado = document.getElementById("idEmpleado").value;
    const fechaInicio = document.getElementById("fechaInicio").value;
    const fechaFin = document.getElementById("fechaFin").value;

    const totalLiquido = parseFloat(document.getElementById("totalLiquidoHidden").value.replace(',', '.')) || 0;

    const lineaNominas = [];

    // Primero añadimos el salario base
    const salarioBaseDiv = document.querySelector("#salario-base-container");
    const salarioBaseConceptoId = salarioBaseDiv.querySelector("input[type='hidden'][name='conceptoId']").value;
    const salarioBaseCantidad = parseFloat(salarioBaseDiv.querySelector("input[name='cantidad']").value.replace(',', '.'));

    lineaNominas.push({
        idConcepto: salarioBaseConceptoId,
        cantidad: salarioBaseCantidad,
        porcentaje: 0
    });

    // Luego añadimos los demás conceptos
    document.querySelectorAll("#conceptos-container > div.concepto-item").forEach(div => {
        const selectElement = div.querySelector("select[name='conceptoId']");
        if (selectElement && selectElement.value) {
            const conceptoId = selectElement.value;
            const cantidadValue = div.querySelector("input[name='cantidad']").value.replace(',', '.');
            const porcentajeValue = div.querySelector("input[name='porcentaje']").value.replace(',', '.');

            lineaNominas.push({
                idConcepto: conceptoId,
                cantidad: parseFloat(cantidadValue) || 0,
                porcentaje: parseFloat(porcentajeValue) || 0
            });
        }
    });

    const dto = {
        idEmpleado: idEmpleado,
        periodo: {
            fechaInicio: fechaInicio,
            fechaFin: fechaFin
        },
        totalLiquido: totalLiquido,
        lineaNominas: lineaNominas
    };

    fetch("/adminapp/guardar-nomina", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dto)
    })
        .then(res => {
            if (res.ok) {
                alert("Nómina guardada correctamente");
                console.log(JSON.stringify(dto, null, 2));
            } else {
                return res.text().then(text => { throw new Error(text); });
            }
        })
        .catch(err => {
            console.error("Error al guardar la nómina:", err);

            try {
                const errorJson = JSON.parse(err.message);

                // Si contiene una lista de errores, los mostramos en el alert
                if (errorJson.listaErrores && Array.isArray(errorJson.listaErrores)) {
                    const mensajePersonalizado = "Se encontraron los siguientes errores:\n- " + errorJson.listaErrores.join("\n- ");
                    alert(mensajePersonalizado);
                } else if (errorJson.message) {
                    alert("Error al guardar la nómina: " + errorJson.message);
                }
            } catch (e) {
                alert("Error inesperado al guardar la nómina: " + err.message);
            }
        });

});

// Inicializar cuando se carga la página
document.addEventListener("DOMContentLoaded", function() {
    inicializarSalarioBase();
});