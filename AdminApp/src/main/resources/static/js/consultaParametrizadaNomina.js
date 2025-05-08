document.addEventListener('DOMContentLoaded', function () {
    const aplicarBtn = document.getElementById('aplicarFiltros');
    const limpiarBtn = document.getElementById('limpiarFiltros');

    aplicarBtn.addEventListener('click', function () {
        const nombre = document.getElementById('filtroNombre').value;
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

        // Redirigir a la búsqueda parametrizada
        window.location.href = '/nomina/busqueda-parametrizada?' + params.toString();
    });

    limpiarBtn.addEventListener('click', function () {
        document.getElementById('filtroNombre').value = '';
        document.getElementById('filtroMes').value = '';
        document.getElementById('filtroAnio').value = '';
        document.getElementById('filtroLiquidoMinimo').value = '';
        document.getElementById('filtroLiquidoMaximo').value = '';
        const conceptos = document.getElementById('conceptos');
        for (let i = 0; i < conceptos.options.length; i++) {
            conceptos.options[i].selected = false;
        }

        // Redirigir al listado completo
        window.location.href = '/nomina/listado';
    });
});
