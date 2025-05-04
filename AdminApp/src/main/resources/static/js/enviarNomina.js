document.getElementById("formulario").addEventListener("submit", function (e) {
    e.preventDefault();

    const idEmpleado = document.getElementById("idEmpleado").value;
    const mes = document.getElementById("mes").value;
    const anio = document.getElementById("anio").value;
    const totalLiquido = parseFloat(document.getElementById("totalLiquidoHidden").value || "0");

    const lineaNominas = [];
    document.querySelectorAll("#conceptos-container > div").forEach(div => {
        const conceptoId = div.querySelector("select[name='conceptoId']").value;
        const cantidad = div.querySelector("input[name='cantidad']").value;
        if (conceptoId && cantidad) {
            lineaNominas.push({
                idConcepto: conceptoId,
                cantidad: parseFloat(cantidad)
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
            } else {
                return res.text().then(text => { throw new Error(text); });
            }
        })
        .catch(err => {
            console.error("Error al guardar la nómina:", err);
            alert("Error al guardar la nómina");
        });
});
