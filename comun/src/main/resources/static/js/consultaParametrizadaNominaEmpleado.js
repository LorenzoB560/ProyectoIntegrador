document.addEventListener('DOMContentLoaded', function () {
    const aplicarBtn = document.getElementById('aplicarFiltros');
    const limpiarBtn = document.getElementById('limpiarFiltros');
    const paginationContainer = document.getElementById("pagination");

    if (aplicarBtn && limpiarBtn) {
        aplicarBtn.addEventListener('click', function () {
            const fechaInicio = document.getElementById('fechaInicio').value;
            const fechaFin = document.getElementById('fechaFin').value;

            const params = new URLSearchParams();

            if (fechaInicio) params.append('fechaInicio', fechaInicio);
            if (fechaFin) params.append('fechaFin', fechaFin);

            window.location.href = '/nomina/busqueda-parametrizada-empleado?' + params.toString();
        });

        limpiarBtn.addEventListener('click', function () {
            document.getElementById('fechaInicio').value = '';
            document.getElementById('fechaFin').value = '';

            window.location.href = '/nomina/listado';
        });
    }

    // Paginación
    if (paginationContainer) {
        const totalPaginas = parseInt(paginationContainer.getAttribute("data-total-paginas"), 10);
        const paginaActual = parseInt(paginationContainer.getAttribute("data-pagina-actual"), 10);
        const modo = paginationContainer.getAttribute("data-modo");
        const queryString = paginationContainer.getAttribute("data-query-string") || "";

        if (totalPaginas > 1) {
            function createPagination() {
                paginationContainer.innerHTML = "";

                const baseUrl = modo === "parametrizada"
                    ? "/nomina/busqueda-parametrizada-empleado"
                    : "/nomina/listado-empleado";

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
