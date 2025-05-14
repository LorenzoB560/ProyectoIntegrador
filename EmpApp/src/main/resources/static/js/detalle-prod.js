document.addEventListener('DOMContentLoaded', () => {
    const cargandoDiv = document.getElementById('cargandoDetalleProd');
    const errorDiv = document.getElementById('errorDetalleProd');
    const detalleContenedor = document.getElementById('detalleProductoContenedor');

    if (typeof productoId === 'undefined' || productoId === null) {
        mostrarErrorDetalleProd('Error: No se pudo obtener el ID del producto desde la página.');
        return;
    }

    if (cargandoDiv) cargandoDiv.style.display = 'block';
    if (errorDiv) errorDiv.style.display = 'none';
    if (detalleContenedor) detalleContenedor.style.display = 'none';

    const url = `/productos/detalle/${productoId}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || `Error del servidor: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (cargandoDiv) cargandoDiv.style.display = 'none';
            if (detalleContenedor) detalleContenedor.style.display = 'block';

            // --- Información General del Producto ---

            rellenarTexto('prod-nombre-header', data.nombre || data.descripcion || 'Producto sin nombre');
            rellenarTexto('prod-descripcion', data.descripcion);
            rellenarTexto('prod-precio', formatearMoneda(data.precio));
            rellenarTexto('prod-marca', data.marca); // Puede ser más específico de ciertos tipos
            rellenarTexto('prod-material', data.material); // Puede ser más específico de ciertos tipos

            // --- Otros Detalles (comunes) ---
            rellenarTexto('prod-unidades', data.unidades);
            rellenarTexto('prod-segunda-mano', data.segundaMano ? 'Sí' : 'No');
            const valoracionSpan = document.getElementById('prod-valoracion');
            if (valoracionSpan) {
                valoracionSpan.innerHTML = generarEstrellasValoracion(data.valoracion);
            }
            rellenarTexto('prod-fecha-fabricacion', formatearFecha(data.fechaFabricacion));
            rellenarTexto('prod-fecha-alta', formatearFecha(data.fechaAlta));

            const coloresSpan = document.getElementById('prod-colores');
            if (coloresSpan) {
                if (data.colores && data.colores.length > 0) {
                    coloresSpan.innerHTML = data.colores.map(color => {
                        if (typeof color === 'string') {
                            return `<span class="badge me-1" style="background-color:${color.toLowerCase()}; color: ${getContrastingTextColor(color.toLowerCase())};">${color}</span>`;
                        } else if (typeof color === 'object' && color !== null) {
                            const bgColor = color.hex || color.codigo || 'grey';
                            const textColor = getContrastingTextColor(bgColor);
                            return `<span class="badge me-1" style="background-color:${bgColor}; color: ${textColor};">${color.nombre || bgColor}</span>`;
                        }
                        return '';
                    }).join(' ');
                } else {
                    coloresSpan.textContent = 'No especificados';
                }
            }

            // --- Proveedor ---
            if (data.proveedor) {
                rellenarTexto('prov-nombre', data.proveedor.nombre);
                rellenarTexto('prov-id', data.proveedor.id);
            } else {
                rellenarTexto('prov-nombre', 'N/A');
                rellenarTexto('prov-id', 'N/A');
            }

            // --- Categorías ---
            const categoriasDiv = document.getElementById('prod-categorias');
            let tipoProductoPrincipal = 'Desconocido'; // Para identificar el tipo y mostrar detalles específicos
            if (categoriasDiv) {
                categoriasDiv.innerHTML = '';
                if (data.categoria && data.categoria.length > 0) {
                    // Asumimos que la primera categoría puede indicar el tipo principal, o necesitas otra lógica/campo
                    tipoProductoPrincipal = data.categoria[0].nombre.toUpperCase();

                    data.categoria.forEach(cat => {
                        const p = document.createElement('p');
                        p.innerHTML = `<span class="badge bg-secondary">${cat.nombre || 'Categoría sin nombre'} (ID: ${cat.id || 'N/A'})</span>`;
                        categoriasDiv.appendChild(p);
                    });
                } else {
                    categoriasDiv.innerHTML = '<p>No hay categorías asignadas.</p>';
                }
            }
            // Mostrar nombre del tipo en el header de la sección de detalles específicos
            rellenarTexto('prod-tipo-nombre-header', capitalizarPrimeraLetra(tipoProductoPrincipal));


            // --- Mostrar Detalles Específicos del Tipo ---
            mostrarDetallesPorTipo(tipoProductoPrincipal, data);

        })
        .catch(error => {
            console.error('Error al cargar detalle del producto:', error);
            mostrarErrorDetalleProd(`Error al cargar datos del producto: ${error.message}`);
        });
});

function mostrarDetallesPorTipo(tipoProducto, data) {
    // Ocultar todas las secciones específicas primero
    document.querySelectorAll('.detalles-tipo-especifico').forEach(el => el.style.display = 'none');
    const cardDetallesTipo = document.getElementById('card-detalles-tipo-especifico');

    let seMostroAlgoEspecifico = false;

    // La comparación de tipoProducto debe ser robusta (ej. toUpperCase)
    switch (tipoProducto.toUpperCase()) {
        case 'MUEBLE':
            const divMueble = document.getElementById('detalles-mueble');
            if (divMueble) {
                let infoMuebleMostrada = false;

                // Dimensiones
                if (data.dimension) {
                    const dim = data.dimension;
                    const dimTexto = `${dim.ancho || 'N/A'}cm x ${dim.profundo || 'N/A'}cm x ${dim.alto || 'N/A'}cm`;
                    // Solo marcar como mostrado si al menos una dimensión es válida (no solo N/A cm x N/A cm x N/A cm)
                    if (dim.ancho || dim.profundo || dim.alto) {
                        if (rellenarTexto('mueble-dimensiones', dimTexto) !== 'N/A') infoMuebleMostrada = true;
                    } else {
                        rellenarTexto('mueble-dimensiones', 'N/A');
                    }
                } else {
                    rellenarTexto('mueble-dimensiones', 'N/A');
                }

                // Material específico para Mueble (puede ser el mismo que el general o uno más detallado)
                // data.material ya se muestra en la sección general. Si quieres mostrarlo de nuevo o un
                // campo diferente como data.materialMuebleEspecifico, ajústalo.
                if (rellenarTexto('mueble-material', data.material) !== 'N/A') infoMuebleMostrada = true;


                // Colores específicos para Mueble
                const coloresMuebleSpan = document.getElementById('mueble-colores');
                if (coloresMuebleSpan) {
                    if (data.colores && Array.isArray(data.colores) && data.colores.length > 0) {
                        coloresMuebleSpan.innerHTML = data.colores.map(color => {
                            if (typeof color === 'string') {
                                return `<span class="badge me-1" style="background-color:${color.toLowerCase()}; color: ${getContrastingTextColor(color.toLowerCase())};">${color}</span>`;
                            } else if (typeof color === 'object' && color !== null) {
                                const bgColor = color.hex || color.codigo || 'grey';
                                const textColor = getContrastingTextColor(bgColor);
                                return `<span class="badge me-1" style="background-color:${bgColor}; color: ${textColor};">${color.nombre || bgColor}</span>`;
                            }
                            return '';
                        }).join(' ');
                        infoMuebleMostrada = true;
                    } else {
                        coloresMuebleSpan.textContent = 'No especificados';
                        // Si quieres considerar "No especificados" como información mostrada, pon infoMuebleMostrada = true;
                        // Si no, y no hay colores, no se marcará como mostrado y el bloque podría no aparecer si nada más se rellena.
                        // Para el JSON de ejemplo que tiene colores: [], esto mostrará "No especificados".
                    }
                }


                if (infoMuebleMostrada) {
                    divMueble.style.display = 'block';
                    seMostroAlgoEspecifico = true;
                }
            }
            break;
        case 'LIBRO':
            const divLibro = document.getElementById('detalles-libro');
            if (divLibro) {
                let infoLibroMostrada = false;

                // Usar los campos del JSON: autor, editorial, numPaginas
                if (rellenarTexto('libro-autor', data.autor) !== 'N/A') infoLibroMostrada = true;
                if (rellenarTexto('libro-editorial', data.editorial) !== 'N/A') infoLibroMostrada = true;
                if (rellenarTexto('libro-paginas', data.numPaginas ? `${data.numPaginas} páginas` : null) !== 'N/A') infoLibroMostrada = true;

                if (infoLibroMostrada) {
                    divLibro.style.display = 'block';
                    seMostroAlgoEspecifico = true;
                }
            }
            break;
        case 'ELECTRÓNICO': // Con tilde
        case 'ELECTRONICO': // Sin tilde, para ser robusto
            const divElectronico = document.getElementById('detalles-electronico');
            if (divElectronico) {
                let infoElectronicoMostrada = false;

                if (rellenarTexto('electronico-modelo', data.modelo) !== 'N/A') infoElectronicoMostrada = true;
                if (rellenarTexto('electronico-garantia-texto', data.garantia) !== 'N/A') infoElectronicoMostrada = true; // data.garantia es string "3 años"
                if (rellenarTexto('electronico-pulgadas', data.pulgadas ? `${data.pulgadas}"` : null) !== 'N/A') infoElectronicoMostrada = true;

                if (data.dimension) {
                    const dim = data.dimension;
                    if (rellenarTexto('electronico-dimensiones', `${dim.ancho || 'N/A'}cm x ${dim.profundo || 'N/A'}cm x ${dim.alto || 'N/A'}cm`) !== 'N/A') infoElectronicoMostrada = true;
                } else {
                    rellenarTexto('electronico-dimensiones', 'N/A');
                }

                if (rellenarTexto('electronico-bateria', data.capacidadBateria ? `${data.capacidadBateria} mAh` : null) !== 'N/A') infoElectronicoMostrada = true;
                if (rellenarTexto('electronico-almacenamiento', data.almacenamientoInterno ? `${data.almacenamientoInterno} GB` : null) !== 'N/A') infoElectronicoMostrada = true;
                if (rellenarTexto('electronico-ram', data.ram ? `${data.ram} GB` : null) !== 'N/A') infoElectronicoMostrada = true;



                if (infoElectronicoMostrada) {
                    divElectronico.style.display = 'block';
                    seMostroAlgoEspecifico = true;
                }
            }
            break;
        case 'ROPA':
            const divRopa = document.getElementById('detalles-ropa');
            if (divRopa) {
                let infoRopaMostrada = false; // Flag local para el bloque ROPA

                // --- AJUSTE PARA TALLAS CON EL NUEVO JSON ---
                const tallasSpan = document.getElementById('ropa-talla');
                if (tallasSpan) {
                    // El campo en el JSON es "tallas" (plural) y cada objeto tiene una propiedad "talla" (singular)
                    if (data.tallas && Array.isArray(data.tallas) && data.tallas.length > 0) {
                        tallasSpan.innerHTML = data.tallas.map(objTalla =>
                            // Accedemos a objTalla.talla para obtener el valor "L", "XL", etc.
                            `<span class="badge bg-light text-dark border me-1">${objTalla.talla || 'Talla'}</span>`
                        ).join(' ');
                        infoRopaMostrada = true; // Se mostró información de tallas
                    } else {
                        tallasSpan.textContent = 'N/A';
                    }
                }
                // --- FIN AJUSTE PARA TALLAS ---

                // Para 'ropa-composicion', asumiendo que tu JSON de ROPA podría tener un campo como 'composicionTextil' o similar.
                // Si el JSON de ejemplo ("Abrigo de invierno") no tiene 'composicionTextil' explícitamente,
                // puedes usar el campo 'material' que sí está presente, o añadir 'composicionTextil' a tu DTO/JSON.
                // Aquí usaré 'data.material' como fallback si 'data.composicionTextil' no existe.
                const composicion = data.composicionTextil || data.material; // Usar 'material' si 'composicionTextil' no está.
                if (rellenarTexto('ropa-composicion', composicion) !== 'N/A') {
                    infoRopaMostrada = true; // Se mostró información de composición/material
                }

                if (infoRopaMostrada) {
                    divRopa.style.display = 'block';
                    seMostroAlgoEspecifico = true; // Actualizar el flag global
                }
            }
            break;
        // Añadir más casos para otros tipos de producto
        default:
            console.log(`No hay detalles específicos definidos para el tipo: ${tipoProducto}`);
            break;
    }

    if (cardDetallesTipo) {
        cardDetallesTipo.style.display = seMostroAlgoEspecifico ? 'block' : 'none';
    }
}

// ... (El resto de tus funciones auxiliares: rellenarTexto, capitalizarPrimeraLetra, formatearMoneda, etc., deben permanecer igual) ...

// Asegúrate que la función rellenarTexto devuelva el valor que se asignó o 'N/A'
// para que la comprobación if (rellenarTexto(...) !== 'N/A') funcione si la usas.
function rellenarTexto(idElemento, valor) {
    const elemento = document.getElementById(idElemento);
    const valorAMostrar = (valor !== null && valor !== undefined && valor !== '') ? String(valor) : 'N/A';
    if (elemento) {
        elemento.textContent = valorAMostrar;
    } else {
        console.warn(`Elemento con ID '${idElemento}' no encontrado en el DOM.`);
    }
    return valorAMostrar; // Devolver lo que se intentó mostrar
}

function capitalizarPrimeraLetra(string) {
    if (!string) return '';
    string = String(string).toLowerCase();
    return string.charAt(0).toUpperCase() + string.slice(1);
}


// --- Funciones auxiliares (formatearMoneda, formatearFecha, generarEstrellasValoracion, getContrastingTextColor, mostrarErrorDetalleProd) ---
// (Mantener las que te proporcioné en la respuesta anterior, ya que son útiles aquí también)

function formatearMoneda(cantidad) {
    if (cantidad === undefined || cantidad === null || isNaN(cantidad)) return 'N/A';
    try {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR',
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(cantidad);
    } catch (e) {
        console.warn("Error formateando moneda:", cantidad, e);
        return 'N/A';
    }
}

function formatearFecha(textoFecha) {
    if (!textoFecha) return 'N/A';
    try {
        const fecha = new Date(textoFecha);
        if (isNaN(fecha.getTime())) {
            return textoFecha;
        }
        return fecha.toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    } catch (e) {
        console.warn("Error formateando fecha:", textoFecha, e);
        return textoFecha;
    }
}

function generarEstrellasValoracion(valoracion) {
    const maxEstrellas = 5;
    let estrellasHtml = '';
    if (valoracion === null || valoracion === undefined || isNaN(valoracion)) {
        return 'N/A';
    }
    const numValoracion = Number(valoracion);
    for (let i = 1; i <= maxEstrellas; i++) {
        if (i <= numValoracion) {
            estrellasHtml += '<i class="bi bi-star-fill"></i>';
        } else {
            estrellasHtml += '<i class="bi bi-star"></i>';
        }
    }
    return estrellasHtml + ` (${numValoracion.toFixed(1)}/${maxEstrellas})`;
}

function getContrastingTextColor(hexColor) {
    if (!hexColor) return '#000000';
    const commonColorsToHex = {
        black: "#000000", white: "#ffffff", red: "#ff0000", green: "#008000", blue: "#0000ff",
        yellow: "#ffff00", cyan: "#00ffff", magenta: "#ff00ff", gray: "#808080",
        maroon: "#800000", olive: "#808000", purple: "#800080", teal: "#008080", navy: "#000080"
    };
    hexColor = commonColorsToHex[hexColor.toLowerCase()] || hexColor;

    const hex = hexColor.replace('#', '');
    if (hex.length !== 3 && hex.length !== 6) return '#000000'; // Default for invalid hex

    let r, g, b;
    if (hex.length === 3) {
        r = parseInt(hex[0] + hex[0], 16);
        g = parseInt(hex[1] + hex[1], 16);
        b = parseInt(hex[2] + hex[2], 16);
    } else {
        r = parseInt(hex.substring(0, 2), 16);
        g = parseInt(hex.substring(2, 4), 16);
        b = parseInt(hex.substring(4, 6), 16);
    }

    if (isNaN(r) || isNaN(g) || isNaN(b)) return '#000000'; // Default if parsing failed

    const yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000;
    return (yiq >= 128) ? '#000000' : '#FFFFFF';
}

function mostrarErrorDetalleProd(mensaje) {
    const errorDiv = document.getElementById('errorDetalleProd');
    const cargandoDiv = document.getElementById('cargandoDetalleProd');
    const detalleContenedor = document.getElementById('detalleProductoContenedor');

    if (cargandoDiv) cargandoDiv.style.display = 'none';
    if (detalleContenedor) detalleContenedor.style.display = 'none';
    if (errorDiv) {
        errorDiv.textContent = mensaje;
        errorDiv.style.display = 'block';
    }
}