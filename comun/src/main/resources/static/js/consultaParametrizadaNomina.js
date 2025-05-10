document.addEventListener('DOMContentLoaded', function () {
    const aplicarBtn = document.getElementById('aplicarFiltros');
    const limpiarBtn = document.getElementById('limpiarFiltros');
    const paginationContainer = document.getElementById("pagination");

    if (aplicarBtn && limpiarBtn) {
        aplicarBtn.addEventListener('click', function () {
            const nombre = document.getElementById('filtroNombre').value;
            const mes = document.getElementById('filtroMes').value;
            const anio = document.getElementById('filtroAnio').value;
            let min = document.getElementById('filtroLiquidoMinimo').value;
            let max = document.getElementById('filtroLiquidoMaximo').value;
            const conceptos = Array.from(document.getElementById('conceptosSeleccionados').selectedOptions).map(opt => opt.text);

            const params = new URLSearchParams();

            if (nombre) params.append('filtroNombre', nombre);
            if (mes) params.append('filtroMes', mes);
            if (anio) params.append('filtroAnio', anio);

            // ✅ Validar valores negativos en los filtros
            min = parseFloat(min);
            max = parseFloat(max);

            if (!isNaN(min) && min >= 0) params.append('totalLiquidoMin', min);
            if (!isNaN(max) && max >= 0) params.append('totalLiquidoMax', max);

            if (!isNaN(min) && !isNaN(max) && min > max) {
                alert("El total líquido mínimo no puede ser mayor que el máximo.");
                return;
            }

            conceptos.forEach(c => params.append('conceptosSeleccionados', c));

            window.location.href = '/nomina/busqueda-parametrizada?' + params.toString();
        });

        limpiarBtn.addEventListener('click', function () {
            document.getElementById('filtroNombre').value = '';
            document.getElementById('filtroMes').value = '';
            document.getElementById('filtroAnio').value = '';
            document.getElementById('filtroLiquidoMinimo').value = '';
            document.getElementById('filtroLiquidoMaximo').value = '';
            const conceptos = document.getElementById('conceptosSeleccionados');
            for (let i = 0; i < conceptos.options.length; i++) {
                conceptos.options[i].selected = false;
            }

            window.location.href = '/nomina/listado';
        });
    }

    // ✅ Actualizar la paginación
    if (paginationContainer) {
        const totalPaginas = parseInt(paginationContainer.getAttribute("data-total-paginas"), 10);
        const paginaActual = parseInt(paginationContainer.getAttribute("data-pagina-actual"), 10);
        const modo = paginationContainer.getAttribute("data-modo");
        const queryString = paginationContainer.getAttribute("data-query-string") || "";

        if (totalPaginas > 1) {
            function createPagination() {
                paginationContainer.innerHTML = "";

                const baseUrl = modo === "parametrizada"
                    ? "/nomina/busqueda-parametrizada"
                    : "/nomina/listado";

                const prevPageItem = document.createElement("li");
                prevPageItem.classList.add("page-item");
                if (paginaActual === 0) prevPageItem.classList.add("disabled");

                const prevLink = document.createElement("a");
                prevLink.classList.add("page-link");
                prevLink.href = `${baseUrl}?page=${paginaActual - 1}${queryString}`;
                prevLink.textContent = "Ant";
                prevPageItem.appendChild(prevLink);
                paginationContainer.appendChild(prevPageItem);

                for (let i = 0; i < totalPaginas; i++) {
                    const pageItem = document.createElement("li");
                    pageItem.classList.add("page-item");
                    if (i === paginaActual) pageItem.classList.add("active");

                    const pageLink = document.createElement("a");
                    pageLink.classList.add("page-link");
                    pageLink.href = `${baseUrl}?page=${i}${queryString}`;
                    pageLink.textContent = i + 1;
                    pageItem.appendChild(pageLink);
                    paginationContainer.appendChild(pageItem);
                }

                const nextPageItem = document.createElement("li");
                nextPageItem.classList.add("page-item");
                if (paginaActual === totalPaginas - 1) nextPageItem.classList.add("disabled");

                const nextLink = document.createElement("a");
                nextLink.classList.add("page-link");
                nextLink.href = `${baseUrl}?page=${paginaActual + 1}${queryString}`;
                nextLink.textContent = "Sig";
                nextPageItem.appendChild(nextLink);
                paginationContainer.appendChild(nextPageItem);
            }

            createPagination();
        }
    }
});
