document.addEventListener('DOMContentLoaded', function () {
    const aplicarBtn = document.getElementById('aplicarFiltros');
    const limpiarBtn = document.getElementById('limpiarFiltros');
    const paginationContainer = document.getElementById("pagination");

    if (aplicarBtn && limpiarBtn) {
        aplicarBtn.addEventListener('click', function () {
            const nombre = document.getElementById('filtroNombre').value;
            const mes = document.getElementById('filtroMes').value;
            const anio = document.getElementById('filtroAnio').value;
            const min = document.getElementById('filtroLiquidoMinimo').value ?
                parseFloat(document.getElementById('filtroLiquidoMinimo').value) : null;

            const max = document.getElementById('filtroLiquidoMaximo').value ?
                parseFloat(document.getElementById('filtroLiquidoMaximo').value) : null;

            const conceptos = Array.from(document.getElementById('conceptosSeleccionados').selectedOptions).map(opt => opt.text);

            const params = new URLSearchParams();
            if (nombre) params.append('filtroNombre', nombre);
            if (mes) params.append('filtroMes', mes);
            if (anio) params.append('filtroAnio', anio);
            if (min !== null) params.append('filtroLiquidoMinimo', min.toString());
            if (max !== null) params.append('filtroLiquidoMaximo', max.toString());
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

    <!-- Paginación para la búsqueda parametrizada-->
    // if (paginationContainer) {
    //     const totalPaginas = parseInt(paginationContainer.getAttribute("data-total-paginas"), 10);
    //     const paginaActual = parseInt(paginationContainer.getAttribute("data-pagina-actual"), 10);
    //
    //     if (totalPaginas > 1) {
    //         function createPagination() {
    //             paginationContainer.innerHTML = "";
    //
    //             const prevPageItem = document.createElement("li");
    //             prevPageItem.classList.add("page-item");
    //             if (paginaActual === 0) prevPageItem.classList.add("disabled");
    //
    //             const prevLink = document.createElement("a");
    //             prevLink.classList.add("page-link");
    //             prevLink.href = `/nomina/busqueda-parametrizada?page=${paginaActual - 1}`;
    //             prevLink.textContent = "Prev";
    //             prevPageItem.appendChild(prevLink);
    //             paginationContainer.appendChild(prevPageItem);
    //
    //             for (let i = 0; i < totalPaginas; i++) {
    //                 const pageItem = document.createElement("li");
    //                 pageItem.classList.add("page-item");
    //
    //                 if (i === paginaActual) pageItem.classList.add("active");
    //
    //                 const pageLink = document.createElement("a");
    //                 pageLink.classList.add("page-link");
    //                 pageLink.href = `/nomina/busqueda-parametrizada?page=${i}`;
    //                 pageLink.textContent = i + 1;
    //
    //                 pageItem.appendChild(pageLink);
    //                 paginationContainer.appendChild(pageItem);
    //             }
    //
    //             const nextPageItem = document.createElement("li");
    //             nextPageItem.classList.add("page-item");
    //             if (paginaActual === totalPaginas - 1) nextPageItem.classList.add("disabled");
    //
    //             const nextLink = document.createElement("a");
    //             nextLink.classList.add("page-link");
    //             nextLink.href = `/nomina/busqueda-parametrizada?page=${paginaActual + 1}`;
    //             nextLink.textContent = "Next";
    //             nextPageItem.appendChild(nextLink);
    //             paginationContainer.appendChild(nextPageItem);
    //         }
    //
    //         createPagination();
    //     }
    // }
});
