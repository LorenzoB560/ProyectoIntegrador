document.addEventListener('DOMContentLoaded', function () {

    document.getElementById('aplicarFiltros').addEventListener('click', function () {
        console.log("Botón de aplicar filtros clickeado"); // Verifica si el evento se activa

        const nombre = document.getElementById('nombre').value;
        const mes = document.getElementById('filtroMes').value;
        const anio = document.getElementById('filtroAnio').value;
        const min = document.getElementById('filtroLiquidoMinimo').value;
        const max = document.getElementById('filtroLiquidoMaximo').value;
        const conceptos = Array.from(document.getElementById('conceptos').selectedOptions).map(opt => opt.text);

        const params = new URLSearchParams();
        if (nombre) params.append('nombre', nombre);
        if (mes) params.append('mes', mes);
        if (anio) params.append('anio', anio);
        if (min) params.append('totalLiquidoMin', min);
        if (max) params.append('totalLiquidoMax', max);
        conceptos.forEach(c => params.append('conceptos', c));

        window.location.href = '/nomina/busqueda-parametrizada?' + params.toString();
    });
});
