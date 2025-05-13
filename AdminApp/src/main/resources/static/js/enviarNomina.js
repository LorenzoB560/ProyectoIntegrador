document.getElementById("formulario").addEventListener("submit", function (e) {
    e.preventDefault();

    const idEmpleado = document.getElementById("idEmpleado").value;
    const mes = document.getElementById("mes").value;
    const anio = document.getElementById("anio").value;
    const totalLiquido = parseFloat(document.getElementById("totalLiquidoHidden").value || "0");

    const lineaNominas = [];

    // Primero añadimos el salario base
    const salarioBaseDiv = document.querySelector("#salario-base-container");
    const salarioBaseConceptoId = salarioBaseDiv.querySelector("input[type='hidden'][name='conceptoId']").value;
    const salarioBaseCantidad = parseFloat(salarioBaseDiv.querySelector("input[name='cantidad']").value);

    lineaNominas.push({
        idConcepto: salarioBaseConceptoId,
        cantidad: salarioBaseCantidad,
        porcentaje: 0
    });

    // Luego añadimos los demás conceptos
    document.querySelectorAll("#conceptos-container > div.concepto-item").forEach(div => {
        const conceptoId = div.querySelector("select[name='conceptoId']").value;
        const cantidad = div.querySelector("input[name='cantidad']").value;
        const porcentaje = div.querySelector("input[name='porcentaje']").value;

        if (conceptoId && cantidad) {
            lineaNominas.push({
                idConcepto: conceptoId,
                cantidad: parseFloat(cantidad),
                porcentaje: parseFloat(porcentaje) || 0
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
                window.location.href = "/adminapp/nomina/listado"; // Redirigir al listado después de guardar
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