package org.grupob.adminapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.grupob.adminapp.converter.*;
import org.grupob.adminapp.dto.masiva.*;
import org.grupob.comun.entity.*;
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.grupob.comun.entity.maestras.Categoria;
import org.grupob.comun.entity.maestras.Proveedor;
import org.grupob.comun.exception.CargaMasivaException;
import org.grupob.comun.exception.CategoriaNoEncontradaException;
import org.grupob.comun.repository.*;
import org.grupob.comun.repository.maestras.CategoriaRepository;
import org.grupob.comun.repository.maestras.ProveedorRepository;
import org.grupob.comun.repository.maestras.TallaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoMasivoService {

    private final LibroConverter libroConverter;
    private final ElectronicoConverter electronicoConverter;
    private final RopaConverter ropaConverter;
    private final MuebleConverter muebleConverter;

    private final ProductoRepository productoRepository;
    private final LibroRepository libroRepository;
    private final ElectronicoRepository electronicoRepository;
    private final RopaRepository ropaRepository;
    private final MuebleRepository muebleRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final Validator validator;

    public ProductoMasivoService(
            LibroConverter libroConverter,
            ElectronicoConverter electronicoConverter,
            RopaConverter ropaConverter,
            MuebleConverter muebleConverter,
            ProductoRepository productoRepository,
            LibroRepository libroRepository,
            ElectronicoRepository electronicoRepository,
            RopaRepository ropaRepository,
            MuebleRepository muebleRepository,
            CategoriaRepository categoriaRepository,
            ProveedorRepository proveedorRepository,
            Validator validator) {
        this.libroConverter = libroConverter;
        this.electronicoConverter = electronicoConverter;
        this.ropaConverter = ropaConverter;
        this.muebleConverter = muebleConverter;
        this.productoRepository = productoRepository;
        this.libroRepository = libroRepository;
        this.electronicoRepository = electronicoRepository;
        this.ropaRepository = ropaRepository;
        this.muebleRepository = muebleRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.validator = validator;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cargaMasiva(InputStream jsonInput) throws CargaMasivaException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // Habilita LocalDate y JsonTypeInfo.Id.DEDUCTION

        try {
            // Procesamiento usando estructura de rootNode
            JsonNode rootNode = mapper.readTree(jsonInput);

            // Obtener información del proveedor
            String nombreProveedor = rootNode.get("proveedor").asText();

            // Buscar o crear proveedor
            Proveedor proveedor = buscarOCrearProveedor(nombreProveedor);

            // Procesar productos
            JsonNode productosNode = rootNode.get("productos");
            if (productosNode != null && productosNode.isArray()) {

                for (JsonNode productoNode : productosNode) {
                    // Convertir el nodo a DTO usando deducción automática
                    ProductoCargaDTO dto = mapper.treeToValue(productoNode, ProductoCargaDTO.class);

                    // Asignar proveedor
                    dto.setProveedor(nombreProveedor);

                    // Validar DTO
                    validarProductoDTO(dto);

                    // Buscar o crear categorías
                    Set<Categoria> categorias = buscarOCrearCategorias(dto.getCategorias());

                    // Verificar si el producto ya existe para ese proveedor
                    if (productoRepository.existsByDescripcionAndProveedorNombre(dto.getDescripcion(), nombreProveedor)) {
                        actualizarProductoExistente(dto, proveedor);
                    } else {
                        crearNuevoProducto(dto, proveedor, categorias);
                    }
                }
            }

        } catch (JsonProcessingException e) {
            throw new CargaMasivaException("Error en el formato o contenido del archivo JSON: " + e.getMessage());
        } catch (IOException e) {
            throw new CargaMasivaException("Error leyendo el archivo: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new CargaMasivaException("Error interactuando con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            throw new CargaMasivaException("Error inesperado durante la carga masiva: " + e.getMessage());
        }
    }

    private void validarProductoDTO(ProductoCargaDTO dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new CargaMasivaException(
                    "Error de validación: " + errorMsg);
        }
    }

    private Proveedor buscarOCrearProveedor(String nombreProveedor) {
        return proveedorRepository.findByNombre(nombreProveedor)
                .orElseGet(() -> {
                    Proveedor nuevoProveedor = new Proveedor();
                    nuevoProveedor.setNombre(nombreProveedor);
                    return proveedorRepository.save(nuevoProveedor);
                });
    }

    private Set<Categoria> buscarOCrearCategorias(Set<String> nombresCategorias) {
        return nombresCategorias.stream()
                .map(nombre -> categoriaRepository.findByNombre(nombre)
                        .orElseGet(() -> {
                            Categoria nuevaCategoria = new Categoria();
                            nuevaCategoria.setNombre(nombre);
                            return categoriaRepository.save(nuevaCategoria);
                        }))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void actualizarProductoExistente(ProductoCargaDTO dto, Proveedor proveedor) {
        Producto producto = productoRepository.findByDescripcionAndProveedorNombre(
                        dto.getDescripcion(), proveedor.getNombre())
                .orElseThrow(() -> new IllegalStateException(
                        "Producto no encontrado después de verificar existencia"));

        // Actualizar campos según la lógica de negocio
        producto.setPrecio(dto.getPrecio());

        // Sumar unidades nuevas
        int unidadesActuales = producto.getUnidades() != null ? producto.getUnidades() : 0;
        producto.setUnidades(unidadesActuales + dto.getUnidades());

        // Si unidades era 0, actualizar fecha de fabricación
        if (unidadesActuales == 0) {
            producto.setFechaFabricacion(dto.getFechaFabricacion());
        }

        productoRepository.save(producto);
    }

    private void crearNuevoProducto(ProductoCargaDTO dto, Proveedor proveedor, Set<Categoria> categorias) {
        // La primera categoría determina el tipo de producto
        String categoriaPrincipal = dto.getCategorias().iterator().next();

        switch (dto) {
            case LibroCargaDTO libroDTO -> {
                validarCategoria(categoriaPrincipal, "LIBRO");
                Libro libro = libroConverter.convertirAEntidad(libroDTO);
                libro.setProveedor(proveedor);
                libro.setCategoria(categorias);
                libro.setFechaAlta(LocalDate.now());
                libro.setValoracion(0);
                libroRepository.save(libro);
            }
            case ElectronicoCargaDTO electronicoDTO -> {
                validarCategoria(categoriaPrincipal, "ELECTRONICO");
                Electronico electronico = electronicoConverter.convertirAEntidad(electronicoDTO);
                electronico.setProveedor(proveedor);
                electronico.setCategoria(categorias);
                electronico.setFechaAlta(LocalDate.now());
                electronico.setValoracion(0);
                electronicoRepository.save(electronico);
            }
            case RopaCargaDTO ropaDTO -> {
                validarCategoria(categoriaPrincipal, "ROPA");
                Ropa ropa = ropaConverter.convertirAEntidad(ropaDTO);
                ropa.setProveedor(proveedor);
                ropa.setCategoria(categorias);
                ropa.setFechaAlta(LocalDate.now());
                ropa.setValoracion(0);
                ropaRepository.save(ropa);
            }
            case MuebleCargaDTO muebleDTO -> {
                validarCategoria(categoriaPrincipal, "MUEBLE");
                Mueble mueble = muebleConverter.convertirAEntidad(muebleDTO);
                mueble.setProveedor(proveedor);
                mueble.setCategoria(categorias);
                mueble.setFechaAlta(LocalDate.now());
                mueble.setValoracion(0);
                muebleRepository.save(mueble);
            }
            default ->
                    throw new IllegalArgumentException("Tipo de producto desconocido: " + dto.getClass().getSimpleName());
        }
    }

    private void validarCategoria(String categoriaRecibida, String tipoEsperado) {
        if (!categoriaRecibida.equalsIgnoreCase(tipoEsperado)) {
            throw new IllegalArgumentException(
                    "La categoría principal '" + categoriaRecibida +
                            "' no coincide con el tipo de producto '" + tipoEsperado + "'"
            );
        }
    }

    @Transactional // <-- ¡Esta anotación es obligatoria para ejecutar consultas de borrado/actualización!
    public void borradoMasivo(String opcion){

        if(opcion.equals(0)){
            eliminarTodos();
        }else{
            eliminarPorCategoria(opcion);
        }

    }

    // Elimina todos los productos
    private void eliminarTodos() {
        productoRepository.deleteAll();
    }

    private void eliminarPorCategoria(String  categoriaId) {
        Long id;
        try {
            id = Long.parseLong(categoriaId);
        } catch (NumberFormatException e) {
            throw new CategoriaNoEncontradaException("Formato no aceptado");
        }
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNoEncontradaException("No existe esa categoria");
        }
        productoRepository.deleteByCategoriaId(id);
    }
}