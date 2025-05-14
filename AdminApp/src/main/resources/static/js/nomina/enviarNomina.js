document.getElementById("formulario").addEventListener("submit", function (e) {
    e.preventDefault();

    //Obtengo todos los valores de
    const idEmpleado = document.getElementById("idEmpleado").value;
    const mes = document.getElementById("mes").value;
    const anio = document.getElementById("anio").value;
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
        mes: parseInt(mes),
        anio: parseInt(anio),
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
                window.location.href = "/nomina/listado";
            } else {
                return res.text().then(text => { throw new Error(text); });
            }
        })
        .catch(err => {
            console.error("Error al guardar la nómina:", err);
            alert("Error al guardar la nómina: " + err.message);
        });
});

// Inicializar cuando se carga la página
document.addEventListener("DOMContentLoaded", function() {
    inicializarSalarioBase();
});