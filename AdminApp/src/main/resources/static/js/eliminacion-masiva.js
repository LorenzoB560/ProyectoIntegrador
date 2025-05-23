// eliminacion-masiva.js
$(document).ready(function() {
    const API_CATEGORIAS = '/adminapp/productos/categorias';
    const API_ELIMINAR = '/adminapp/productos/borrado-masivo';
    const $select = $('#selectCategorias');
    const $form = $('#formBorradoProductos');
    const $mensajeDiv = $('#mensaje');

    // Cargar categorías al iniciar
    cargarCategorias();

    function cargarCategorias() {
        $.ajax({
            url: API_CATEGORIAS,
            method: 'GET',
            success: function(categorias) {
                if(categorias.length === 0) {
                    mostrarError('No hay categorías disponibles');
                    return;
                }
                $select.empty();
                $select.append('<option value="0">Todos los productos</option>');
                $.each(categorias, function(_, cat) {
                    $select.append(
                        $('<option>', { value: cat.id, text: cat.nombre })
                    );
                });
            },
            error: function() {
                mostrarError('Error al cargar categorías');
            }
        });
    }

    $form.on('submit', function(event) {
        event.preventDefault();
        const categoria = $select.val();

        if (!confirm('¿Eliminar los productos seleccionados?')) return;

        $.ajax({
            url: API_ELIMINAR + '?categoria=' + encodeURIComponent(categoria),
            method: 'DELETE',
            success: function() {
                mostrarExito('Productos eliminados correctamente');
            },
            error: function(xhr) {
                let mensaje = 'Error en la eliminación';
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    mensaje = xhr.responseJSON.message;
                }
                mostrarError(mensaje);
            }
        });
    });

    function mostrarExito(mensaje) {
        $mensajeDiv.text(mensaje)
            .removeClass('mensaje-error')
            .addClass('mensaje-exito');
    }

    function mostrarError(mensaje) {
        $mensajeDiv.text(mensaje)
            .removeClass('mensaje-exito')
            .addClass('mensaje-error');
    }
});
